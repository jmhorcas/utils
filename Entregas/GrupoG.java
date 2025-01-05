package org.mps.netflix;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FavoritosTest {

    @InjectMocks
    private Favoritos favoritos;

    @Mock
    private Programa programaMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        favoritos = new Favoritos();
    }

    @Test
    @DisplayName("Añadir un programa a la lista de favoritos")
    void testNuevoPrograma() {
        when(programaMock.getId()).thenReturn(1);

        favoritos.nuevoPrograma(programaMock);

        assertEquals(1, favoritos.totalProgramas(), "La lista de favoritos debería contener un programa");
        verify(programaMock, times(1)).getId();
    }

    @Test
    @DisplayName("Añadir un programa null debe lanzar excepción")
    void testNuevoProgramaNull() {
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> favoritos.nuevoPrograma(null), "Debería lanzar excepción para programa null");
        assertEquals("Información no validada", exception.getMessage());
    }

    @Test
    @DisplayName("Actualizar un programa existente debe reemplazarlo")
    void testNuevoProgramaExistente() {
        when(programaMock.getId()).thenReturn(1);

        favoritos.nuevoPrograma(programaMock);

        Programa otroProgramaMock = mock(Programa.class);
        when(otroProgramaMock.getId()).thenReturn(1);

        favoritos.nuevoPrograma(otroProgramaMock);

        assertEquals(1, favoritos.totalProgramas(), "La lista debería tener un solo programa después del reemplazo");
        verify(otroProgramaMock, times(1)).getId();
    }

    @Test
    @DisplayName("Actualizar el minuto con valor mayor que la duración lanza excepción")
    void testActualizarMinutoMayorQueDuracion() {
        when(programaMock.getId()).thenReturn(1);
        when(programaMock.getDuracion()).thenReturn(50);

        favoritos.nuevoPrograma(programaMock);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> favoritos.actualizarMinuto(1, 60), "El minuto no debe superar la duración del programa");
        assertEquals("El minuto actual no puede ser mayor que la duración total del programa", exception.getMessage());
    }

    @Test
    @DisplayName("Actualizar minuto negativo lanza excepción")
    void testActualizarMinutoNegativo() {
        when(programaMock.getId()).thenReturn(1);

        favoritos.nuevoPrograma(programaMock);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> favoritos.actualizarMinuto(1, -5), "El minuto no puede ser negativo");
        assertEquals("El minuto no puede ser negativo", exception.getMessage());
    }

    @Test
    @DisplayName("Actualizar minuto de un programa inexistente lanza excepción")
    void testActualizarMinutoProgramaNoExistente() {
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> favoritos.actualizarMinuto(1, 10), "Debería lanzar excepción para programa no existente");
        assertEquals("El programa no existe", exception.getMessage());
    }

    @Test
    @DisplayName("Eliminar un programa existente debe reducir el tamaño de la lista")
    void testEliminarPrograma() {
        when(programaMock.getId()).thenReturn(1);

        favoritos.nuevoPrograma(programaMock);
        boolean eliminado = favoritos.eliminarPrograma(1);

        assertTrue(eliminado, "El programa debería eliminarse correctamente");
        assertEquals(0, favoritos.totalProgramas(), "La lista debería estar vacía después de eliminar");
    }

    @Test
    @DisplayName("Eliminar un programa no existente lanza excepción")
    void testEliminarProgramaNoExistente() {
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> favoritos.eliminarPrograma(999), "Debería lanzar excepción para programa inexistente");
        assertEquals("El programa no ha sido eliminado", exception.getMessage());
    }

    @Test
    @DisplayName("Intentar ver un programa inexistente lanza excepción")
    void testVerProgramaNoExistente() {
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> favoritos.verPrograma(1), "Debería lanzar excepción para programa no existente");
        assertEquals("Ese programa no existe en la lista", exception.getMessage());
    }

    @Test
    @DisplayName("Ver un programa existente")
    void testVerProgramaExistente() {
        when(programaMock.getId()).thenReturn(1);
        favoritos.nuevoPrograma(programaMock);

        Programa result = favoritos.verPrograma(1);

        assertEquals(programaMock, result, "Debería devolver el programa correctamente");
    }

    @Test
    @DisplayName("Obtener programas empezados devuelve lista correcta")
    void testEmpezados() {
        when(programaMock.getId()).thenReturn(1);
        when(programaMock.getMinutoActual()).thenReturn(10);

        Programa otroProgramaMock = mock(Programa.class);
        when(otroProgramaMock.getId()).thenReturn(2);
        when(otroProgramaMock.getMinutoActual()).thenReturn(0);

        favoritos.nuevoPrograma(programaMock);
        favoritos.nuevoPrograma(otroProgramaMock);

        List<Programa> empezados = favoritos.empezados();

        assertEquals(1, empezados.size(), "Solo un programa debería estar en la lista de empezados");
        assertTrue(empezados.contains(programaMock), "El programa que empezó debería estar en la lista");
    }

    @Test
    @DisplayName("Obtener programas empezados con múltiples entradas")
    void testEmpezadosConMultiplesProgramas() {
        Programa programa1 = mock(Programa.class);
        Programa programa2 = mock(Programa.class);
        Programa programa3 = mock(Programa.class);

        when(programa1.getId()).thenReturn(1);
        when(programa1.getMinutoActual()).thenReturn(10);

        when(programa2.getId()).thenReturn(2);
        when(programa2.getMinutoActual()).thenReturn(0);

        when(programa3.getId()).thenReturn(3);
        when(programa3.getMinutoActual()).thenReturn(5);

        favoritos.nuevoPrograma(programa1);
        favoritos.nuevoPrograma(programa2);
        favoritos.nuevoPrograma(programa3);

        List<Programa> empezados = favoritos.empezados();

        assertEquals(2, empezados.size(), "Deberían haber dos programas en la lista de empezados");
        assertTrue(empezados.contains(programa1), "Programa 1 debería estar en la lista");
        assertTrue(empezados.contains(programa3), "Programa 3 debería estar en la lista");
    }

    @Test
    @DisplayName("Intentar obtener programas empezados sin ninguno empezado lanza excepción")
    void testEmpezadosSinProgramas() {
        RuntimeException exception = assertThrows(RuntimeException.class,
                favoritos::empezados, "Debería lanzar excepción si no hay programas empezados");
        assertEquals("No se encontraron programas empezados", exception.getMessage());
    }

    @Test
    @DisplayName("Actualizar los títulos con un patrón")
    void testActualizarTitulosConPatron() {
        // Mockeamos la interfaz Programa
        Programa programa1 = mock(Programa.class);
        Programa programa2 = mock(Programa.class);

        // Establecemos el comportamiento de los mocks
        when(programa1.getId()).thenReturn(1);
        when(programa1.getTitulo()).thenReturn("Título Viejo");
        when(programa2.getId()).thenReturn(2);
        when(programa2.getTitulo()).thenReturn("Otro Título");

        // Simulamos que setTitulo modifica el título
        doAnswer(invocation -> {
            String newTitulo = invocation.getArgument(0);
            when(programa1.getTitulo()).thenReturn(newTitulo);
            return null;
        }).when(programa1).setTitulo(anyString());

        // Agregar los programas a favoritos
        favoritos.nuevoPrograma(programa1);
        favoritos.nuevoPrograma(programa2);

        // Actualizar títulos con patrón
        favoritos.actualizarTitulosConPatron("Viejo", "Nuevo Título");

        // Verificamos que el título se haya actualizado correctamente
        assertEquals("Nuevo Título", programa1.getTitulo(), "El título debería haberse actualizado");
        assertEquals("Otro Título", programa2.getTitulo(), "El título del programa que no coincide no debería cambiar");
    }


}
