package org.mps.netflix;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FavoritosTest {

    private Favoritos favoritos;

    @Mock
    private Programa programaMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        favoritos = new Favoritos();
    }

    @Test
    @DisplayName("Agregar un programa nuevo")
    void agregarNuevoPrograma() {
        // Arrange
        when(programaMock.getId()).thenReturn(1);

        // Act
        favoritos.nuevoPrograma(programaMock);

        // Assert
        assertEquals(1, favoritos.totalProgramas());
        assertEquals(programaMock, favoritos.verPrograma(1));
    }

    @Test
    @DisplayName("Actualizar un programa existente")
    void actualizarProgramaExistente() {
        // Arrange
        when(programaMock.getId()).thenReturn(1);
        favoritos.nuevoPrograma(programaMock);
        Programa programaActualizado = mock(Programa.class);
        when(programaActualizado.getId()).thenReturn(1);

        // Act
        favoritos.nuevoPrograma(programaActualizado);

        // Assert
        assertEquals(1, favoritos.totalProgramas());
        assertEquals(programaActualizado, favoritos.verPrograma(1));
    }

    @Test
    @DisplayName("No se puede agregar un programa nulo")
    void agregarProgramaNuloLanzaExcepcion() {
        // Act & Assert
        assertThrows(RuntimeException.class, () -> favoritos.nuevoPrograma(null));
    }

    @Test
    @DisplayName("Actualizar minuto de un programa existente")
    void actualizarMinutoProgramaExistente() {
        // Arrange
        when(programaMock.getId()).thenReturn(1);
        favoritos.nuevoPrograma(programaMock);

        // Act
        favoritos.actualizarMinuto(1, 120);

        // Assert
        verify(programaMock).setMinutoActual(120);
    }

    @Test
    @DisplayName("Actualizar minuto de un programa inexistente lanza excepción")
    void actualizarMinutoProgramaInexistenteLanzaExcepcion() {
        // Act & Assert
        assertThrows(RuntimeException.class, () -> favoritos.actualizarMinuto(1, 120));
    }

    @Test
    @DisplayName("Obtener programas empezados")
    void obtenerProgramasEmpezados() {
        // Arrange
        when(programaMock.getId()).thenReturn(1);
        when(programaMock.getMinutoActual()).thenReturn(10);
        favoritos.nuevoPrograma(programaMock);

        // Act
        List<Programa> empezados = favoritos.empezados();

        // Assert
        assertEquals(1, empezados.size());
        assertEquals(programaMock, empezados.get(0));
    }

    @Test
    @DisplayName("Eliminar un programa existente")
    void eliminarProgramaExistente() {
        // Arrange
        when(programaMock.getId()).thenReturn(1);
        favoritos.nuevoPrograma(programaMock);

        // Act
        boolean eliminado = favoritos.eliminarPrograma(1);

        // Assert
        assertTrue(eliminado);
        assertEquals(0, favoritos.totalProgramas());
    }

    @Test
    @DisplayName("Eliminar un programa inexistente no afecta la lista")
    void eliminarProgramaInexistente() {
        // Act
        boolean eliminado = favoritos.eliminarPrograma(1);

        // Assert
        assertFalse(eliminado);
        assertEquals(0, favoritos.totalProgramas());
    }

    @Test
    @DisplayName("Actualizar títulos con un patrón")
    void actualizarTitulosConPatron() {
        // Arrange
        when(programaMock.getTitulo()).thenReturn("Mi Programa");
        when(programaMock.getId()).thenReturn(1);
        favoritos.nuevoPrograma(programaMock);

        // Act
        favoritos.actualizarTitulosConPatron("Mi", "Nuevo Título");

        // Assert
        verify(programaMock).setTitulo("Nuevo Título");
    }
    


    @Test
    @DisplayName("Lanzar excepción al intentar ver un programa inexistente")
    void verProgramaInexistenteLanzaExcepcion() {
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> favoritos.verPrograma(999));
        assertEquals("Ese programa no existe en la lista", exception.getMessage());
}
@Test
@DisplayName("Obtener un programa existente")
void verProgramaExistente() {
    // Arrange
    when(programaMock.getId()).thenReturn(1);
    favoritos.nuevoPrograma(programaMock);

    // Act
    Programa programa = favoritos.verPrograma(1);

    // Assert
    assertEquals(programaMock, programa);
}


  
    
}

