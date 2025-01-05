package org.mps.netflix;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProgramaTest {

    @Test
    @DisplayName("El constructor  crea una lista vacia")
    public void FavoritosConstructor(){
        //Arrange. Preparar el estado del codigo  a probar (Crear objetos , inicializar variables)
        Favoritos favoritos = new Favoritos();
        //Act. Ejecutar la logica del sistema a probar
        //Assert. Ejecutar las pruebas y comprobar si hay errores 
        assertEquals(0  , favoritos.totalProgramas());
        
    }

    @Test
    @DisplayName("Tienen que insertar un programa en la lista creada")
    public void PruebaNuevoPrograma(){
        //Arrange
        Programa nuevo=mock(Programa.class);    
        //Mock
        when(nuevo.getId()).thenReturn(1);
        when(nuevo.getTitulo()).thenReturn("Mock Title");
        Favoritos lista=new Favoritos();
        //Act
        lista.nuevoPrograma(nuevo);

        //Assert
        assertEquals(1, lista.totalProgramas());
        assertEquals(nuevo, lista.verPrograma(1));
    }

    @Test
    @DisplayName("La informacion no es valida")
    public void PruebaNuevoInvalidoPrograma(){
        //Arrange
        Favoritos favoritos=new Favoritos();
        //Act
        Exception exception = assertThrows(RuntimeException.class, () -> favoritos.nuevoPrograma(null));     
        //Assert
        assertEquals("Informacion no validad", exception.getMessage());

    }
    @Test
    @DisplayName("Deberia reemplazar el programa si ya existe con el mismo. getId()")
    public void pruebaReemplazoProgramaExistente() {
        // Arrange
        Favoritos listaFavoritos = new Favoritos();
        int nuevo2min=90;

        Programa nuevo1 = mock(Programa.class);
        when(nuevo1.getId()).thenReturn(1);
        when(nuevo1.getMinutoActual()).thenReturn(30);
        Programa nuevo2 = mock(Programa.class);
        when(nuevo2.getId()).thenReturn(1);  // Mismo ID
        when(nuevo2.getMinutoActual()).thenReturn(nuevo2min);  // Minuto diferente
        
        // Act
        listaFavoritos.nuevoPrograma(nuevo1);
        listaFavoritos.nuevoPrograma(nuevo2);
        
        // Assert
        assertEquals(1, listaFavoritos.totalProgramas()); //Ya que no se ha insertado sino cambiado
        assertSame(listaFavoritos.verPrograma(nuevo2.getId()),listaFavoritos.verPrograma(nuevo1.getId()));
        
    }


    @Test
    @DisplayName("Devuelve el minuto en el que se ha quedado el usuario")
    public void PruebaActualizarMinuto(){
        //Arrange
        int minuto=0;
        int minuto_actual=90;
        Programa nuevo=mock(Programa.class);
        Favoritos lista=new Favoritos();
        when(nuevo.getMinutoActual()).thenReturn(minuto);
        when(nuevo.getId()).thenReturn(1);

        //Act
        lista.nuevoPrograma(nuevo);
        lista.actualizarMinuto(nuevo.getId(), minuto_actual);
        //Assert
        verify(nuevo, times(1)).setMinutoActual(minuto_actual);
        assertEquals(0, nuevo.getMinutoActual());
    }

    @Test
    @DisplayName("Sacamos las excepcion si no se ha encontrado un programa real")
    public void PruebaActualizarMinuto1(){
        //Arrange
        int minuto=0;
        int minuto_actual=90;
        int id_buscado=1;
        Programa nuevo=mock(Programa.class);
        Favoritos lista=new Favoritos();
        //Como se comporta el mock
        when(nuevo.getMinutoActual()).thenReturn(minuto);
        when(nuevo.getId()).thenReturn(2);
        //Act
        lista.nuevoPrograma(nuevo);
        lista.actualizarMinuto(nuevo.getId(), minuto_actual);
        //Assert
        Exception exception = assertThrows(RuntimeException.class, () -> lista.actualizarMinuto(id_buscado, minuto_actual));     
        assertEquals("El programa no existe", exception.getMessage());
    }

    @Test
    @DisplayName("Tiene que devolver el total de los programas correctamente")
    public void PruebaTotalProgramas(){
        //Arrange
        Programa nuevo1=mock(Programa.class);
        Programa nuevo2=mock(Programa.class);

        when(nuevo1.getId()).thenReturn(1);
        when(nuevo1.getTitulo()).thenReturn("Mock Title1");

        when(nuevo2.getId()).thenReturn(2);
        when(nuevo2.getTitulo()).thenReturn("Mock Title2");
        Favoritos lista=new Favoritos();

        //Act
        lista.nuevoPrograma(nuevo1);
        lista.nuevoPrograma(nuevo2);
        //Assert
        assertEquals(2, lista.totalProgramas());
    
    }

    @Test
    @DisplayName("Devuelve los titulos de un programa actualizado")
    public void PruebaActualizarTitulosPatron(){
        //Arrange
        String patron="Titulo2";
        String titulo="TituloNuevo";

        Programa nuevo1=mock(Programa.class);
        Programa nuevo2=mock(Programa.class);
        Favoritos lista=new Favoritos();

        when(nuevo1.getTitulo()).thenReturn("Titulo1");
        when(nuevo2.getTitulo()).thenReturn("Titulo2");
                
        lista.nuevoPrograma(nuevo1);
        lista.nuevoPrograma(nuevo2);

        //ACT
        lista.actualizarTitulosConPatron(patron, titulo);
        
        //Assert
        verify(nuevo1, never()).setTitulo(anyString());
        verify(nuevo2, times(1)).setTitulo(titulo);
        assertEquals("Titulo1", nuevo1.getTitulo()); // No cambia

    }
    @Test
    @DisplayName("No actualiza títulos si ningun programa coincide con el patron_nuevo")
    public void PruebaActualizarTitulosSinCoincidencia() {
        // Arrange
        String patron = "NoExiste";
        String tituloNuevo = "TituloNuevo";
    
        
        Programa nuevo1 = mock(Programa.class);
        Programa nuevo2 = mock(Programa.class);
        Favoritos lista = new Favoritos();
        //Mocks
        when(nuevo1.getTitulo()).thenReturn("Titulo1");
        when(nuevo2.getTitulo()).thenReturn("Titulo2");

        lista.nuevoPrograma(nuevo1);
        lista.nuevoPrograma(nuevo2);
    
        // Act
        lista.actualizarTitulosConPatron(patron, tituloNuevo);
    
        // Assert
        verify(nuevo1, never()).setTitulo(anyString());
        verify(nuevo2, never()).setTitulo(anyString());
        assertEquals("Titulo1", nuevo1.getTitulo());
        assertEquals("Titulo2", nuevo2.getTitulo());
    }

    @Test
    @DisplayName("Devuelve un programa que no existe")
    public void PruebaVerPrograma1(){
        //Arrange
        Programa nuevo1=mock(Programa.class);
        Programa nuevo2=mock(Programa.class);
        int id_buscado=3;
        Favoritos lista=new Favoritos();
        //mock
        when(nuevo1.getId()).thenReturn(1);
        when(nuevo1.getTitulo()).thenReturn("Titulo1");

        when(nuevo2.getId()).thenReturn(2);
        when(nuevo2.getTitulo()).thenReturn("Titulo2");

        lista.nuevoPrograma(nuevo1);
        lista.nuevoPrograma(nuevo2);
        //act
        Exception exception = assertThrows(RuntimeException.class, () -> lista.verPrograma(id_buscado));     
        //Assert
        assertEquals("Ese programa no existe en la lista", exception.getMessage());
    }
    @Test
    @DisplayName("Devuelve true si se  ha eliminado correctamente el programa con un id")
    public void PruebaEliminarPrograma(){
        //Arrange
        Programa nuevo1=mock(Programa.class);
        Programa nuevo2=mock(Programa.class);

        when(nuevo1.getId()).thenReturn(1);
        when(nuevo1.getTitulo()).thenReturn("Titulo1");

        when(nuevo2.getId()).thenReturn(2);
        when(nuevo2.getTitulo()).thenReturn("Titulo2");

        Favoritos lista=new Favoritos();
        lista.nuevoPrograma(nuevo1);
        lista.nuevoPrograma(nuevo2);
        //Act y Assert

       assertTrue(lista.eliminarPrograma(nuevo1.getId()));
       assertFalse(lista.eliminarPrograma(nuevo1.getId())); //Ya que no lo podemos eliminar 2 veces
    }

    @Test
    @DisplayName("Devuelve una lista con programas con mas de 0 minuto")
    public void PruebaEmpezados(){
        //Arrange
        Programa nuevo1=mock(Programa.class);
        Programa nuevo2=mock(Programa.class);
        Favoritos lista=new Favoritos();

        when(nuevo1.getId()).thenReturn(1);
        when(nuevo1.getMinutoActual()).thenReturn(20);

        when(nuevo2.getId()).thenReturn(2);
        when(nuevo2.getMinutoActual()).thenReturn(0);


        lista.nuevoPrograma(nuevo1);
        lista.nuevoPrograma(nuevo2);
        //Act
        List<Programa>res=lista.empezados();
        //Assert
        assertTrue(res.contains(nuevo1));
        assertFalse(res.contains(nuevo2));
    }

}
