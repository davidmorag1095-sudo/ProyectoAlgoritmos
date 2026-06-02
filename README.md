# Proyecto Algoritmos - Grupo 4

Tema asignado: **Tema 4 - Algoritmos recursivos**.

El PDF del proyecto pide desarrollar un programa en Java basado en el Juego de la Vida de Conway, ambientado en una reserva natural costarricense.

## Requisitos principales

- Matriz de 4x4 hasta 8x8.
- El usuario indica el tamano de la matriz.
- El usuario coloca los microorganismos iniciales.
- El usuario indica la cantidad de generaciones.
- En cada generacion se muestra la matriz resultante.
- En cada generacion se muestra el conteo de:
  - microorganismos que sobreviven,
  - microorganismos que nacen,
  - microorganismos que mueren.
- Las zonas de la matriz usan referencias a areas silvestres protegidas de Costa Rica.
- El conteo de vecinos debe hacerse con un metodo recursivo.
- No se debe usar un ciclo `for` o `while` para recorrer las 8 posiciones vecinas.

## Como ejecutar

Desde la raiz del proyecto:

```powershell
javac -encoding UTF-8 -d out src/JuegoDeLaVidaApp.java
java -cp out JuegoDeLaVidaApp
```

## Punto clave para la defensa

El metodo recursivo principal es:

```java
private int contarVecinosRecursivo(boolean[][] matriz, int fila, int columna, int indice)
```

El caso base ocurre cuando `indice == DIRECCIONES_VECINOS.length`. En ese momento ya se revisaron las 8 posiciones vecinas y la recursion termina.

La celda central no se cuenta porque el arreglo `DIRECCIONES_VECINOS` solo contiene desplazamientos vecinos. No incluye `{0, 0}`.
