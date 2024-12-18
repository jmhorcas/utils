import os
import difflib
import pathlib
import argparse


FILE = 'Entregas/GrupoA.java'


def get_filepaths(dir: str, extensions_filter: list[str] = []) -> list[str]:
    """Get all filepaths of files with the given extensions from the given directory."""
    filepaths = []
    for root, dirs, files in os.walk(dir):
        for file in files:
            if not extensions_filter or any(file.endswith(ext) for ext in extensions_filter):
                filepath = os.path.join(root, file)
                filepaths.append(filepath)
    return filepaths



def get_similarity(file1: str, file2: str) -> float:
    with open(file1) as f1, open(file2) as f2:
        content1 = f1.read()
        content2 = f2.read()

    similarity = difflib.SequenceMatcher(None, content1, content2).ratio()
    return similarity


def compare_files(dir: str) -> None:
    files_to_compare = get_filepaths(dir)
    files_to_compare.sort()
    for i, file in enumerate(files_to_compare, 1):
        print(f'{i}: Similarities with {file}:')
        for i2, ftc in enumerate(files_to_compare, 1):
            if file != ftc:
                similarity = get_similarity(file, ftc)
                print(f'  |-{i2}:{ftc}: {similarity * 100:.2f}%')   


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description='Check similarity')
    parser.add_argument(metavar='dir', dest='dir', type=str, help='Directory with files.')
    args = parser.parse_args()

    if os.path.isdir(args.dir):
        compare_files(args.dir)