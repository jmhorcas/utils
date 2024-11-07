import os
import argparse
import subprocess
import pathlib
import glob
import shutil


OUTPUT_FILE = 'output.csv'


class SPIN():

    SPIN_EXE = 'C:/spin/spin.exe'

    def __init__(self, promela_filepath: str, executable_path: str = SPIN_EXE) -> None:
        self._promela_filepath: str = promela_filepath
        self._exe: str = executable_path
        self._ltls = None

    def clean_files(self) -> None:
        files = glob.glob('pan.*') + glob.glob('*.tmp')
        for file in files:
            pathlib.Path(file).unlink()
    
    def _get_ltls(self, stdout: str) -> list[str]:
        ltls_lines = [line for line in stdout.splitlines() if line.startswith('ltl ')]
        ltls = []
        for ltl in ltls_lines:
            ltl_name = ltl.split(' ')[1][:-1]
            ltls.append(ltl_name)
        return ltls
    
    def check_compilation(self) -> bool:
        process = subprocess.Popen([self._exe, '-a', self._promela_filepath], stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True, shell=True)
        stdout, stderr = process.communicate()
        self._ltls = self._get_ltls(stdout)
        success = pathlib.Path('pan.c').is_file()
        return success and not 'Error: ' in stdout

    def check_deadlock(self) -> bool:
        command = ['gcc', '-DMEMLIM=1024', '-O2', '-DXUSAFE', '-DSAFETY', '-DNOCLAIM', '-w', '-o', 'pan', 'pan.c']
        process = subprocess.Popen(command, stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True, shell=True)
        process.wait()
        process = subprocess.Popen(['pan.exe', '-m10000'], stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True, shell=True)
        stdout, stderr = process.communicate()
        state_vector_line = [line for line in stdout.splitlines() if line.startswith('State-vector ')]
        n_errors = int(state_vector_line[0].split(' ')[-1])
        return n_errors > 0

    def check_ltls(self) -> list[tuple[str, bool]]:
        if self._ltls is None:
            self.check_compilation()
        ltls_checks = []
        for ltl in self._ltls:
            process = subprocess.Popen([self._exe, '-search', '-ltl', ltl, self._promela_filepath], stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True, shell=True)
            stdout, stderr = process.communicate()
            state_vector_line = [line for line in stdout.splitlines() if line.startswith('State-vector ')]
            n_errors = int(state_vector_line[0].split(' ')[-1])
            ltls_checks.append((ltl, n_errors == 0))
        return ltls_checks


def get_filepaths(dir: str, extensions_filter: list[str] = []) -> list[str]:
    """Get all filepaths of files with the given extensions from the given directory."""
    filepaths = []
    for root, dirs, files in os.walk(dir):
        for file in files:
            if not extensions_filter or any(file.endswith(ext) for ext in extensions_filter):
                filepath = os.path.join(root, file)
                filepaths.append(filepath)
    return filepaths


def main(filepath: str) -> None:
    spin = SPIN(filepath)
    compile = spin.check_compilation()
    print(f'Compila? {compile}')
    if compile:
        deadlock = spin.check_deadlock()
        ltls = spin.check_ltls()
        print(f'Bloquea? {deadlock}')
        print(f'Cumple LTLs? {ltls}')
    spin.clean_files()


def main_folder(folderpath: str) -> None:
    header = 'Alumno, File, Compila, Bloquea, LTLs'
    with open(OUTPUT_FILE, mode='w', encoding='utf8') as file:
        file.write(f'{header}\n')
    promela_files = get_filepaths(folderpath, ['pml'])
    for promela_filepath in promela_files:
        path = pathlib.Path(promela_filepath)
        shutil.copyfile(promela_filepath, path.name)

        alumno = promela_filepath.split(os.sep)[2].split('_')[0]
        print(f'Processing file {path.name} by {alumno}...')
        spin = SPIN(path.name)
        compile = spin.check_compilation()
        deadlock = ''
        ltls = ''
        print(f'|-compile? {compile}')
        if compile:
            deadlock = spin.check_deadlock()
            print(f'|-deadlock? {deadlock}')
            ltls = spin.check_ltls()
        output = f'{alumno}, {path.name}, {compile}, {deadlock}, {'; '.join([f"{ltl[0]}: {ltl[1]}" for ltl in ltls])}'
        with open(OUTPUT_FILE, mode='a', encoding='utf8') as file:
            file.write(f'{output}\n')
        spin.clean_files()
        pathlib.Path(path.name).unlink()
    

if __name__ == '__main__':
    parser = argparse.ArgumentParser(description='Ejecuta Spin sobre ficheros promela.')
    parser.add_argument(metavar='path', dest='path', type=str, help='Input promela file (.pml) or directory with files.')
    args = parser.parse_args()

    if os.path.isdir(args.path):
        main_folder(args.path)
    else:
        main(args.path)