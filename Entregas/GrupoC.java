package org.mps.netflix;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FavoritosTest {

    private Favoritos favoritos;
    private Programa programaMock;

    @BeforeEach
    void setUp() {
        favoritos = new Favoritos();
        programaMock = mock(Programa.class);
    }

    @Test
    @DisplayName("Nuevo programa se añade correctamente")
    void nuevoPrograma_AnadeCorrectamente() {
        when(programaMock.getId()).thenReturn(1);
        favoritos.nuevoPrograma(programaMock);

        Programa resultado = favoritos.verPrograma(1);
        assertEquals(programaMock, resultado);
    }

    @Test
    @DisplayName("Nuevo programa lanza excepcion si es nulo")
    void nuevoPrograma_LanzaExcepcionSiNulo() {
        Exception exception = assertThrows(RuntimeException.class, () -> favoritos.nuevoPrograma(null));
        assertEquals("Informacion no validad", exception.getMessage());
    }

    @Test
    @DisplayName("Actualizar minuto lanza excepcion si programa no existe")
    void actualizarMinuto_LanzaExcepcionSiNoExiste() {
        Exception exception = assertThrows(RuntimeException.class, () -> favoritos.actualizarMinuto(1, 50));
        assertEquals("El programa no existe", exception.getMessage());
    }

    @Test
    @DisplayName("Actualizar minuto actualiza correctamente")
    void actualizarMinuto_ActualizaCorrectamente() {
        when(programaMock.getId()).thenReturn(1);
        favoritos.nuevoPrograma(programaMock);

        favoritos.actualizarMinuto(1, 50);
        verify(programaMock).setMinutoActual(50);
    }

    @Test
    @DisplayName("Eliminar programa devuelve verdadero si elimina correctamente")
    void eliminarPrograma_DevuelveVerdaderoSiElimina() {
        when(programaMock.getId()).thenReturn(1);
        favoritos.nuevoPrograma(programaMock);

        boolean eliminado = favoritos.eliminarPrograma(1);
        assertTrue(eliminado);
    }

    @Test
    @DisplayName("Eliminar programa devuelve falso si programa no existe")
    void eliminarPrograma_DevuelveFalsoSiNoExiste() {
        boolean eliminado = favoritos.eliminarPrograma(1);
        assertFalse(eliminado);
    }

    @Test
    @DisplayName("Ver programa lanza excepcion si no existe")
    void verPrograma_LanzaExcepcionSiNoExiste() {
        Exception exception = assertThrows(RuntimeException.class, () -> favoritos.verPrograma(1));
        assertEquals("Ese programa no existe en la lista", exception.getMessage());
    }

    @Test
    @DisplayName("Obtener programas empezados devuelve lista correcta")
    void empezados_DevuelveListaCorrecta() {
        Programa programaMock1 = mock(Programa.class);
        Programa programaMock2 = mock(Programa.class);

        when(programaMock1.getMinutoActual()).thenReturn(10);
        when(programaMock2.getMinutoActual()).thenReturn(0);
        when(programaMock1.getId()).thenReturn(1);
        when(programaMock2.getId()).thenReturn(2);

        favoritos.nuevoPrograma(programaMock1);
        favoritos.nuevoPrograma(programaMock2);

        List<Programa> empezados = favoritos.empezados();
        assertEquals(1, empezados.size());
        assertTrue(empezados.contains(programaMock1));
        assertFalse(empezados.contains(programaMock2));
    }

    @Test
    @DisplayName("Actualizar titulos con patron modifica correctamente")
    void actualizarTitulosConPatron_ModificaCorrectamente() {
        when(programaMock.getTitulo()).thenReturn("Comedia Romantica");
        when(programaMock.getId()).thenReturn(1);

        favoritos.nuevoPrograma(programaMock);
        favoritos.actualizarTitulosConPatron("Comedia", "Drama");

        verify(programaMock).setTitulo("Drama");
    }
}
