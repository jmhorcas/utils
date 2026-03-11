from pydriller import Repository
from collections import defaultdict
import re
from datetime import datetime

# --- CONFIGURACIÓN ---
REPO_PATH = "/home/josemi/Downloads/healthcalc/"
EXCLUDE = ["github-classroom[bot]", "dependabot[bot]", "ManagerUser", "José Miguel Horcas"]
ALIASES = {
    "MarioPeralesDiaz": "Mario Perales Díaz",
    "Estela Anaya": "Estela"

}
RAMA_ESPECIFICA = "P1"  # Pon None si quieres todas las ramas
FECHA_INICIO = datetime(2026, 2, 24)  # Año, Mes, Día

# --- CONFIGURACIÓN DE NOTAS ---
NOTA_GRUPAL = 4.5
FACTOR_SENSIBILIDAD = 0.05  # Factor de Sensibilidad
MAX_BONUS = 2.0  # Nadie puede subir más de 2 puntos sobre la nota grupal


# Regex para detectar Conventional Commits (Documentación)
CONVENTIONAL_REGEX = r"^(feat|fix|docs|style|refactor|perf|test|build|ci|chore|revert)(\(.+\))?: .+"

def calcular_esfuerzo():
    raw_stats = defaultdict(lambda: {"commits": 0, "complexity": 0, "doc_score": 0, "atomicity": 0})

    # Aplicamos los filtros en el objeto Repository
    repo = Repository(REPO_PATH, only_in_branch=RAMA_ESPECIFICA, since=FECHA_INICIO)
    
    # Analizar el repositorio
    for commit in repo.traverse_commits():
        author_name = commit.author.name
        
        # Filtrado de Omisión
        if author_name in EXCLUDE:
            continue
            
        # Unificación de Alias
        final_name = ALIASES.get(author_name, author_name)

        raw_stats[final_name]["commits"] += 1
        
        # A. Calidad de Documentación (D)
        msg = commit.msg.strip()
        is_conv = 1 if re.match(CONVENTIONAL_REGEX, msg) else 0
        len_score = min(len(msg) / 60, 1.0)
        raw_stats[final_name]["doc_score"] += (is_conv * 0.7 + len_score * 0.3)

        # B. Atomicidad (A) - Premiamos cambios quirúrgicos sobre megacommits
        raw_stats[final_name]["atomicity"] += 1 if len(commit.modified_files) <= 5 else 0.4

        # C. Complejidad Técnica (I) - Impacto real en la lógica
        cpx = sum(m.complexity or 0 for m in commit.modified_files)
        raw_stats[final_name]["complexity"] += cpx

    # Cálculo de Scores y Porcentajes
    individual_scores = {}
    total_project_score = 0

    for author, data in raw_stats.items():
        # Aplicamos pesos a los totales acumulados
        # Commits (10%) + Complejidad (40%) + Doc (30%) + Atomicidad (20%)
        score = (data["commits"] * 0.1) + \
                (data["complexity"] * 0.4) + \
                (data["doc_score"] * 0.3) + \
                (data["atomicity"] * 0.2)
        
        individual_scores[author] = score
        total_project_score += score

    # --- REPORTE DE RESULTADOS ---
    print(f"\n{'MIEMBRO DEL EQUIPO':<25} | {'SCORE ABSOLUTO':<15} | {'% DE TRABAJO'}")
    print("-" * 65)

    # Ordenar por el que más ha trabajado
    sorted_results = sorted(individual_scores.items(), key=lambda x: x[1], reverse=True)

    for author, score in sorted_results:
        percentage = (score / total_project_score) * 100 if total_project_score > 0 else 0
        print(f"{author:<25} | {score:>15.2f} | {percentage:>11.2f}%")

    NUM_MIEMBROS = len(individual_scores)
    PORCENTAJE_ESPERADO = 100 / NUM_MIEMBROS if NUM_MIEMBROS > 0 else 0

    print(f"\n{'MIEMBRO':<25} | {'% TRABAJO':<12} | {'NOTA FINAL'}")
    print("-" * 55)

    for author, score in sorted_results:
        percentage = (score / total_project_score) * 100
        
        # Cálculo de la nota basada en la desviación
        # Si aporta más del esperado, sube; si aporta menos, baja.
        # El factor 0.1 controla qué tan sensible es la nota a la diferencia.
        desviacion = percentage - PORCENTAJE_ESPERADO
        ajuste = desviacion * FACTOR_SENSIBILIDAD
        
        # Aplicamos el tope de bonus/penalización
        ajuste = max(-MAX_BONUS, min(MAX_BONUS, ajuste))
        
        nota_individual = NOTA_GRUPAL + ajuste
        nota_individual = max(0, min(10, nota_individual))
        
        print(f"{author:<25} | {percentage:>10.2f}% | {nota_individual:>10.2f}")


if __name__ == "__main__":
    calcular_esfuerzo()