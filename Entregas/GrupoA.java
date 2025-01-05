package org.mps.netflix;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FavoritosTest {

  private Favoritos favoritos;

  @BeforeEach
  public void setup() {
    favoritos = new Favoritos() ;
  }

@Test
public void NuevoProgramaNoFuncionaDevuelveExcepcion() {

    // Creamos un programa nulo, por lo que no entra en el if y devuelve una excepción

    // Assert
    assertThrows(RuntimeException.class, () -> favoritos.nuevoPrograma(null));

}

@Test
public void NuevoProgramaFuncionaProgramaNoExistente() {

    // Creamos un programa nuevo, lo agregamos a la lista y por lo tanto el size de la lista debe ser exactamente 1

    // Arrange
    Programa programa = mock(Programa.class);
    when(programa.getId()).thenReturn(1);

    // Act
    favoritos.nuevoPrograma(programa);

    // Assert
    assertEquals(1, favoritos.totalProgramas());
}

@Test
public void NuevoProgramaFuncionaProgramaYaExistente() {

    // Creamos un programa nuevo, lo agregamos a la lista dos veces y por lo tanto el size de la lista debe ser exactamente 1
    // (en la segunda vez, se actualiza)

    // Arrange
    Programa programa1 = mock(Programa.class);
    when(programa1.getId()).thenReturn(1);

    // Act
    favoritos.nuevoPrograma(programa1);
    favoritos.nuevoPrograma(programa1);

    // DEBE DEVOLVER 1, PORQUE AL LLAMARLO DOS VECES NO SE VUELVE A CREAR, SINO QUE SE ACTUALIZA.
    // POR LO TANTO EL TAMAÑO DE LA LISTA DEBE SEGUIR SIENDO 1.

    // Assert
    assertEquals(1, favoritos.totalProgramas());

}

@Test
public void ActualizarMinutoFuncionaProgramaExistente() {
  
    // Arrange
    Programa programa = mock(Programa.class);  
    favoritos.nuevoPrograma(programa);
    when(programa.getId()).thenReturn(1);
    

    // Act
    favoritos.actualizarMinuto(1, 90);

    // Assert
    verify(programa).setMinutoActual(90);

}

@Test
public void ActualizarMinutoNoFuncionaDevuelveExcepcion() {

    // Al buscar un programa que no está en la lista (ya que nunca lo agregamos), devuelve una excepción

    // Assert
    assertThrows(RuntimeException.class, () -> favoritos.actualizarMinuto(2, 60));

}

@Test
public void totalProgramasFuncionaListaVacia() {
  
    // Al consultar el tamaño de la lista sin haber agregado ningún programa, debe dar exactamente 0

    // Act
    int size = favoritos.totalProgramas();

    // Assert
    assertEquals(0, size);

}

@Test
public void totalProgramasFuncionaListaLlena() {

    // Al consultar el tamaño de la lista habiendo agregado dos programas, debe dar exactamente 2
  
    // Arrange
    Programa programa1 = mock(Programa.class);
    Programa programa2 = mock(Programa.class);
    when(programa1.getId()).thenReturn(1);
    when(programa2.getId()).thenReturn(2);
    favoritos.nuevoPrograma(programa1);
    favoritos.nuevoPrograma(programa2);

    // Act
    int size = favoritos.totalProgramas();

    // Assert
    assertEquals(2, size);

}

@Test
public void setTituloFuncionaProgramaExistente() {

    // Al intentar modificar un programa que está en la lista, comprobamos que se invoca al método set
    // NOTA: en este caso usamos verify en vez de assert ya que el método que deseamos probar devuelve void
  
    // Arrange
    Programa programa = mock(Programa.class);
    favoritos.nuevoPrograma(programa);
    when(programa.getTitulo()).thenReturn("Drama");

    // Act
    favoritos.actualizarTitulosConPatron("Drama", "Comedia");

    // Assert
    verify(programa).setTitulo("Comedia");

}

@Test
public void setTituloNoFuncionaProgramaExistente() {

    // Al intentar modificar un programa que está en la lista, pero que no tiene dicho titulo, comprobamos que no se invoca al método set
    // NOTA: en este caso usamos verify en vez de assert ya que el método que deseamos probar devuelve void
  
    // Arrange
    Programa programa = mock(Programa.class);
    favoritos.nuevoPrograma(programa);
    when(programa.getTitulo()).thenReturn("Terror");

    // Act
    favoritos.actualizarTitulosConPatron("Drama", "Comedia");

    // Assert
    verify(programa, times(0)).setTitulo("Comedia");

}

@Test
public void setTituloNoFuncionaProgramaNoExistente() {

    // Al intentar modificar un programa que no está en la lista (ya que nunca lo agregamos), comprobamos que se invoca al método set
    // NOTA: en este caso usamos verify en vez de assert ya que el método que deseamos probar devuelve void
  
    // Arrange
    Programa programa = mock(Programa.class);
    when(programa.getTitulo()).thenReturn("Drama");

    // Act
    favoritos.actualizarTitulosConPatron("Drama", "Comedia");

    // Assert
    verify(programa, times(0)).setTitulo("Comedia");

}

@Test
public void verProgramaFuncionaProgramaExistente() {

    // Al buscar un programa que está en la lista mediante su id, se devuelve correctamente
  
    // Arrange
    Programa programa = mock(Programa.class);
    favoritos.nuevoPrograma(programa);
    when(programa.getId()).thenReturn(1);

    // Act
    Programa resultado = favoritos.verPrograma(1);

    // Assert
    assertEquals(programa, resultado);

}

@Test
public void verProgramaNoFuncionaDevuelveExcepcion() {
  
    // Al buscar un programa que no está en la lista (ya que nunca lo agregamos), devuelve una excepción

    // Assert
    assertThrows(RuntimeException.class, () -> favoritos.verPrograma(1));

}

@Test
public void eliminarProgramaDevuelveTrue() {

    // Al eliminar un programa que está en la lista, devuelve True
  
    // Arrange
    Programa programa = mock(Programa.class);
    favoritos.nuevoPrograma(programa);
    when(programa.getId()).thenReturn(1);

    // Act
    boolean resultado = favoritos.eliminarPrograma(1);

    // Assert
    assertTrue(resultado);

}

@Test
public void eliminarProgramaDevuelveFalse() {

    // Al eliminar un programa que no está en la lista (ya que nunca lo agregamos), devuelve False

    // Act
    boolean resultado = favoritos.eliminarPrograma(1);

    // Assert
    assertFalse(resultado);

}

@Test
public void empezadosDevuelveListaVacia() {

    // Al buscar programas empezados sobre una lista vacía (ya que no agregamos ninguno), devuelve una lista vacía

    // Act
    List<Programa> resultado = favoritos.empezados();
    int sizeResultado = resultado.size();

    // Assert
    assertEquals(0, sizeResultado);

}

@Test
public void empezadosDevuelveListaVacia2() {

    // Al buscar programas empezados sobre una lista que no tiene programas empezados, devuelve una lista vacía
    
    // Arrange
    Programa programa = mock(Programa.class);
    favoritos.nuevoPrograma(programa);
    when(programa.getMinutoActual()).thenReturn(0);

    // Act
    List<Programa> resultado = favoritos.empezados();
    int sizeResultado = resultado.size();

    // Assert
    assertEquals(0, sizeResultado);

}

@Test
public void empezadosDevuelveListaLlena() {

    // Al buscar programas empezados sobre una lista con un único programa empezado, devuelve una lista con un elemento

    // Arrange
    Programa programa1 = mock(Programa.class);
    favoritos.nuevoPrograma(programa1);
    when(programa1.getMinutoActual()).thenReturn(10);

    // Programa programa2 = mock(Programa.class);
    // favoritos.nuevoPrograma(programa2);
    // when(programa2.getMinutoActual()).thenReturn(0);

    // Act
    List<Programa> resultado = favoritos.empezados();
    int sizeResultado = resultado.size();

    // Assert
    assertEquals(1, sizeResultado);

}


}
