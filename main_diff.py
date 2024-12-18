import re
import os
import ast
import difflib
import argparse


def get_filepaths(dir: str, extensions_filter: list[str] = []) -> list[str]:
    """Get all filepaths of files with the given extensions from the given directory."""
    filepaths = []
    for root, dirs, files in os.walk(dir):
        for file in files:
            if not extensions_filter or any(file.endswith(ext) for ext in extensions_filter):
                filepath = os.path.join(root, file)
                filepaths.append(filepath)
    return filepaths


def preprocess_code(content: str) -> str:
    """Preprocess code by removing comments and normalizing content."""
    # Remove single-line comments (//)
    content = re.sub(r'//.*', '', content)
    # Remove multi-line comments (/* */)
    content = re.sub(r'/\*.*?\*/', '', content, flags=re.DOTALL)
    # Remove blank lines and strip whitespaces
    content = "\n".join(line.strip() for line in content.splitlines() if line.strip())
    return content


# def get_similarity(file1: str, file2: str) -> float:
#     with open(file1) as f1, open(file2) as f2:
#         content1 = f1.read()
#         content2 = f2.read()
#     content1 = [line for line in content1 if line.strip()]  # Remove empty lines
#     content2 = [line for line in content2 if line.strip()]  # Remove empty lines

#     similarity = difflib.SequenceMatcher(None, content1, content2).ratio()
#     return similarity

# def get_similarity(file1: str, file2: str) -> float:
#     with open(file1) as f1, open(file2) as f2:
#         content1 = preprocess_code(f1.read())
#         content2 = preprocess_code(f2.read())
    
#     # Tokenize (simple split by whitespace for demonstration; replace with a proper tokenizer)
#     tokens1 = content1.split()
#     tokens2 = content2.split()
    
#     similarity = difflib.SequenceMatcher(None, tokens1, tokens2).ratio()
#     return similarity
from datasketch import MinHash
def get_similarity(file1: str, file2: str) -> float:
    def minhash_from_file(filepath: str) -> MinHash:
        with open(filepath) as f:
            content = preprocess_code(f.read())
            m = MinHash()
            for token in content.split():
                m.update(token.encode('utf8'))
            return m
    
    m1 = minhash_from_file(file1)
    m2 = minhash_from_file(file2)
    
    return m1.jaccard(m2)


def compare_files(dir: str) -> None:
    files_to_compare = get_filepaths(dir)
    files_to_compare.sort()

    rankings = {}  # Ranking individual por archivo
    global_scores = {}  # Puntuación global por archivo

    # Calcular similitudes
    for file in files_to_compare:
        similarities = []
        total_similarity = 0  # Acumular similitudes para el ranking global
        for ftc in files_to_compare:
            if file != ftc:
                similarity = get_similarity(file, ftc)
                similarities.append((ftc, similarity))
                total_similarity += similarity
        rankings[file] = sorted(similarities, key=lambda x: x[1], reverse=True)
        global_scores[file] = total_similarity

    # Número total de archivos
    num_files = len(files_to_compare)
    max_score = num_files - 1  # Máximo posible para la puntuación global

    # Imprimir ranking individual por archivo
    for i, (file, ranking) in enumerate(rankings.items(), 1):
        print(f"{i}: Ranking de similitudes para {file}:")
        for j, (ftc, similarity) in enumerate(ranking, 1):
            print(f"  {j}. {ftc}: {similarity * 100:.2f}%")

    print("\nRanking global de ficheros según similitud acumulada (normalizado):")
    # Ordenar el ranking global
    sorted_global_ranking = sorted(global_scores.items(), key=lambda x: x[1], reverse=True)
    for rank, (file, score) in enumerate(sorted_global_ranking, 1):
        normalized_score = score / max_score if max_score > 0 else 0
        print(f"{rank}. {file}: {normalized_score:.2f} (probabilidad de plagio)")


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description='Check similarity')
    parser.add_argument(metavar='dir', dest='dir', type=str, help='Directory with files.')
    args = parser.parse_args()

    if os.path.isdir(args.dir):
        compare_files(args.dir)