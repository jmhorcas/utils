package org.mps.netflix;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FavoritosTest {
    private Favoritos favoritos;
    private Programa mockPrograma1;
    private Programa mockPrograma2;

    @BeforeEach
    void setUp() {
        favoritos = new Favoritos();
        mockPrograma1 = Mockito.mock(Programa.class);
        mockPrograma2 = Mockito.mock(Programa.class);

        // Configuración inicial de los mocks
        when(mockPrograma1.getId()).thenReturn(1);
        when(mockPrograma1.getMinutoActual()).thenReturn(10);
        when(mockPrograma1.getTitulo()).thenReturn("Programa 1");
        
        when(mockPrograma2.getId()).thenReturn(2);
        when(mockPrograma2.getMinutoActual()).thenReturn(0);
        when(mockPrograma2.getTitulo()).thenReturn("Programa 2");
    }

    @Test
    @DisplayName("Agregar un nuevo programa correctamente")
    void testAgregarNuevoPrograma() {
        // Act
        favoritos.nuevoPrograma(mockPrograma1);

        // Assert
        assertEquals(1, favoritos.totalProgramas());
        assertEquals(mockPrograma1, favoritos.verPrograma(1));
    }

    @Test
    @DisplayName("Actualizar un programa existente")
    void testActualizarProgramaExistente() {
        // Arrange
        favoritos.nuevoPrograma(mockPrograma1);
        when(mockPrograma1.getTitulo()).thenReturn("Programa Actualizado");

        // Act
        favoritos.nuevoPrograma(mockPrograma1);

        // Assert
        assertEquals(1, favoritos.totalProgramas());
        assertEquals("Programa Actualizado", favoritos.verPrograma(1).getTitulo());
    }

    @Test
    @DisplayName("Lanzar excepción al intentar agregar un programa nulo")
    void testAgregarProgramaNulo() {
        // Act & Assert
        assertThrows(RuntimeException.class, () -> favoritos.nuevoPrograma(null));
    }

    @Test
    @DisplayName("Actualizar el minuto actual de un programa existente")
    void testActualizarMinuto() {
        // Arrange
        favoritos.nuevoPrograma(mockPrograma1);

        // Act
        favoritos.actualizarMinuto(1, 50);

        // Assert
        verify(mockPrograma1).setMinutoActual(50);
    }

    @Test
    @DisplayName("Lanzar excepción al actualizar minuto de un programa inexistente")
    void testActualizarMinutoProgramaInexistente() {
        // Act & Assert
        assertThrows(RuntimeException.class, () -> favoritos.actualizarMinuto(3, 20));
    }

    @Test
    @DisplayName("Eliminar un programa existente")
    void testEliminarPrograma() {
        // Arrange
        favoritos.nuevoPrograma(mockPrograma1);

        // Act
        boolean eliminado = favoritos.eliminarPrograma(1);

        // Assert
        assertTrue(eliminado);
        assertEquals(0, favoritos.totalProgramas());
    }

    @Test
    @DisplayName("No eliminar un programa inexistente")
    void testEliminarProgramaInexistente() {
        // Act
        boolean eliminado = favoritos.eliminarPrograma(3);

        // Assert
        assertFalse(eliminado);
    }

    @Test
    @DisplayName("Recuperar programas empezados")
    void testProgramasEmpezados() {
        // Arrange
        favoritos.nuevoPrograma(mockPrograma1);
        favoritos.nuevoPrograma(mockPrograma2);

        // Act
        List<Programa> empezados = favoritos.empezados();

        // Assert
        assertEquals(1, empezados.size());
        assertTrue(empezados.contains(mockPrograma1));
    }

    @Test
    @DisplayName("Actualizar títulos con patrón")
    void testActualizarTitulosConPatron() {
        // Arrange
        favoritos.nuevoPrograma(mockPrograma1);

        // Act
        favoritos.actualizarTitulosConPatron("Programa", "Nuevo Título");

        // Assert
        verify(mockPrograma1).setTitulo("Nuevo Título");
    }

    @Test
    @DisplayName("Lanzar excepción al consultar un programa inexistente")
    void testConsultarProgramaInexistente() {
        // Act & Assert
        assertThrows(RuntimeException.class, () -> favoritos.verPrograma(3));
    }
}
