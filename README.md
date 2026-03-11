# utils
Several utils scripts.


# Metodología de Evaluación de Contribución Individual
Para garantizar una calificación justa y objetiva, se ha implementado un sistema automático de cuantificación del esfuerzo basado en el análisis del historial del repositorio Git. Esta metodología no solo mide la cantidad de cambios, sino también la **calidad técnica, la organización y la documentación** del trabajo aportado por cada miembro.

## 1. Score de Esfuerzo Individual ($S$)
Cada commit realizado se evalúa mediante un algoritmo que analiza cuatro variables críticas. La puntuación total de un desarrollador es la suma ponderada de estos factores:

$$S = (C \cdot 0.1) + (I \cdot 0.4) + (D \cdot 0.3) + (A \cdot 0.2)$$

### Factores de Ponderación:

* **Volumen de Trabajo ($C$: 10%):** Representa el número total de commits. Es una métrica de frecuencia y persistencia en el proyecto.
* **Impacto y Complejidad ($I$: 40%):** Es el factor de mayor peso. Utiliza el análisis de **Complejidad Ciclomática** para medir cuánta lógica real se ha añadido. Un cambio en un algoritmo complejo suma más que la edición de archivos de texto o configuraciones simples.
* **Calidad de Documentación ($D$: 30%):** Evalúa la claridad de los mensajes de commit. Se premia el uso del estándar *Conventional Commits* y la capacidad descriptiva para facilitar la trazabilidad.
* **Atomicidad ($A$: 20%):** Mide la eficiencia en la organización del código. Se otorgan puntuaciones más altas a los commits "atómicos" (pocos archivos modificados con un propósito único) frente a los "megacommits" desordenados.

---

## 2. Porcentaje de Participación Relativa ($P$)
Para entender el peso de cada integrante en el conjunto del proyecto, se calcula el porcentaje de esfuerzo relativo. Este valor representa la "cuota de responsabilidad" técnica de cada miembro sobre el 100% del trabajo total del repositorio.

$$P_i = \left( \frac{S_i}{\sum S_{total}} \right) \cdot 100$$

Este cálculo es fundamental para identificar quién ha liderado el desarrollo técnico y quién ha tenido un rol de soporte.

---

## 3. Cálculo de la Nota Individual ($N_i$)

La nota final de cada alumno se deriva de la **Nota Grupal** obtenida en el proyecto. Se aplica una fórmula de redistribución basada en la desviación respecto a la media de trabajo esperada.

La media esperada ($E$) es el reparto equitativo del trabajo entre los $n$ miembros: $E = 100 / n$.

$$N_i = \text{Nota Grupal} + ((P_i - E) \cdot 0.05)$$

### Decisiones de Diseño de la Fórmula:
* **Factor de Sensibilidad (0.05):** Se ha ajustado este valor para que la nota sea meritocrática pero mantenga la cohesión del equipo. Permite que los alumnos con mayor carga de trabajo vean recompensado su esfuerzo sin que los alumnos con menor carga pierdan el aprobado si el trabajo grupal es sólido.
* **Equilibrio:** Si un alumno aporta exactamente la media esperada ($P_i = E$), su nota individual será idéntica a la grupal.
* **Límites:** La nota final está limitada en un rango de 0 a 10.
* **Tope de Ajuste (Max/Min):** El ajuste máximo (al alza) está limitado a **+2.0 puntos** respecto a la nota grupal. Esto garantiza que la nota individual siga vinculada a la calidad del producto final entregado por el equipo.


---

## 4. Herramientas Utilizadas
El análisis se ha realizado de forma automatizada mediante scripts de Python utilizando:
* **PyDriller:** Para la minería de datos del repositorio Git.
* **Análisis Estático de Código:** Para el cálculo de la complejidad ciclomática de los archivos modificados.
* **Filtrado de Autores:** Exclusión de bots de sistema y unificación de alias para evitar duplicidad de perfiles.
  
### Filtros de Auditoría
Para asegurar que la evaluación corresponde estrictamente al periodo de trabajo actual y al código entregado:
* **Filtro de Rama:** Solo se contabilizan los aportes integrados en la rama `${RAMA_ESPECIFICA}`, garantizando que solo el código validado y fusionado sea evaluado.
* **Filtro Temporal:** El análisis comienza a partir del `${FECHA_INICIO}`, ignorando configuraciones iniciales o trabajos previos al inicio oficial del proyecto.