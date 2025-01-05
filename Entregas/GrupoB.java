package org.mps.netflix;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

class FavTest {
    Favoritos favoritos  ;

  @BeforeEach
  void setup() {
    favoritos = new Favoritos() ;
  }

@Test
@DisplayName("Constructor debería inicializar la lista de programas como vacía")
void constructor_listaVacia() {

    // Arrange
    //Act
    int listaFav = favoritos.totalProgramas();

    // Assert
    assertEquals(0, listaFav, "Después de la inicialización la lista debería de estar vacia");
}

@Test
@DisplayName("Añade un nuevo programa a la lista de Favoritos")
void nuevoPrograma_añade_a_listaFavoritos() {
    // Arrange
    Programa programaMock = mock(Programa.class);  // Crear un mock de Programa
    when(programaMock.getId()).thenReturn(1);  // Configurar el ID del programa
    when(programaMock.getDuracion()).thenReturn(5); // Configurar duración de la peli
    when(programaMock.getGenero()).thenReturn("Comedia"); // Configurar género 
    when(programaMock.getMinutoActual()).thenReturn(3); // Configurar minutos actuales
    when(programaMock.getTitulo()).thenReturn("Dos padres y un niño"); 


    // Act
    favoritos.nuevoPrograma(programaMock);

    // Assert
    assertEquals(1, favoritos.totalProgramas(), "Se ha añadido el programa por eso numTotalProgrmas = 1");
    assertEquals(programaMock, favoritos.verPrograma(1), "El programa que queríamos añadir es el mismo que el insertado a la lista");
}

@Test
@DisplayName("Añade un nuevo programa a la lista de Favoritos")
void nuevoPrograma_modificar_a_listaFavoritos() {
    // Arrange
    Programa programaMock = mock(Programa.class);  // Crear un mock de Programa
    when(programaMock.getId()).thenReturn(1);  // Configurar el ID del programa

    // Act
    favoritos.nuevoPrograma(programaMock);
    favoritos.nuevoPrograma(programaMock);

    // Assert
    assertEquals(1, favoritos.totalProgramas(), "Se ha añadido el programa por eso numTotalProgrmas = 1");
    assertEquals(programaMock, favoritos.verPrograma(1), "El programa que queríamos añadir es el mismo que el insertado a la lista");
}

@Test
@DisplayName("Deberia de dar una excepción si intento añadir un programa Nulo a Favoritos")
void nuevoPrograma_añade_NULO_a_listaFavoritos() {
    // Arrange
    // Act
    // Assert
    assertThrows(RuntimeException.class, ()->favoritos.nuevoPrograma(null));
  }

@Test
@DisplayName("Actualizar minutos del Programa")
void actualizarMinuto_ProgramaExiste() {
    // Arrange
    Programa programaMock = mock(Programa.class);  
    when(programaMock.getId()).thenReturn(1);  // Existe el programa
    favoritos.nuevoPrograma(programaMock);  // Añadir el programa a favoritos

    // Act
    favoritos.actualizarMinuto(1, 42);  // Actualizar el minuto actual

    // Assert
    verify(programaMock).setMinutoActual(42);  // Verificar que se llama al método con el minuto correcto
}

@Test
@DisplayName("Deberia de dar una excepción si intento añadir un programa Nulo a Favoritos")
void actualizarMinuto_ProgramaExiste_Excepcion_NoEXISTE() {
    // Arrange
    // Act
    // Assert
    assertThrows(RuntimeException.class, ()->favoritos.actualizarMinuto(0, 120));
  }

  @Test
@DisplayName("Debería actualizar los títulos de los programas que coinciden con el patrón")
void actualizarTitulosConPatron_deberiaActualizarTitulosConCoincidencias() {
    // Arrange
    Programa programaMock1 = mock(Programa.class);
    Programa programaMock2 = mock(Programa.class);

    when(programaMock1.getTitulo()).thenReturn("Titulo1");
    when(programaMock2.getTitulo()).thenReturn("MiPatronTitulo2");

    favoritos.nuevoPrograma(programaMock1);
    favoritos.nuevoPrograma(programaMock2);

    // Act
    favoritos.actualizarTitulosConPatron("Patron", "NuevoTitulo");

    // Assert
    verify(programaMock1, never()).setTitulo(anyString()); // No debería cambiar el título de programaMock1
    verify(programaMock2).setTitulo("NuevoTitulo"); // Debería cambiar el título de programaMock2
}

@Test
@DisplayName("Deberia de dar una excepción si intento añadir un programa Nulo a Favoritos")
void verPrograma_Excepcion_NoExiste() {
    // Arrange
    // Act
    // Assert
    assertThrows(RuntimeException.class, ()->favoritos.verPrograma(-1));
  }


@Test
@DisplayName("Eliminar programa")
void eliminarPrograma_Si_Existe() {
    // Arrange
    Programa programaMock = mock(Programa.class);
    when(programaMock.getId()).thenReturn(1);  // programa con id = 1
    favoritos.nuevoPrograma(programaMock);  // Añadimos el programa a favoritos

    // Act
    favoritos.eliminarPrograma(1);  // Eliminamos el programa con el ID 1

    // Assert
    assertEquals(0, favoritos.totalProgramas(), "lista vacia después de eliminarlo");
}
@Test
@DisplayName("Debería devolver los programas que ya han sido empezados por el usuario")
void empezados_deberiaDevolverProgramasEmpezados() {
    // Arrange
    Favoritos favoritos = new Favoritos();

    // Creamos dos programas, uno con minutoActual > 0 y otro con minutoActual = 0
    Programa programa1 = mock(Programa.class);
    when(programa1.getMinutoActual()).thenReturn(5);  // Este programa ha sido empezado
    when(programa1.getId()).thenReturn(1);

    Programa programa2 = mock(Programa.class);
    when(programa2.getMinutoActual()).thenReturn(0);  // Este programa no ha sido empezado
    when(programa2.getId()).thenReturn(2);

    favoritos.nuevoPrograma(programa1);
    favoritos.nuevoPrograma(programa2);

    // Act
    List<Programa> resultados = favoritos.empezados();  // Recuperamos los programas empezados

    // Assert
    assertEquals(1, resultados.size(), "Debería haber solo un programa empezado.");
    assertTrue(resultados.contains(programa1), "El programa con minutoActual > 0 debería estar en la lista.");
    assertFalse(resultados.contains(programa2), "El programa con minutoActual = 0 no debería estar en la lista.");
}


}

