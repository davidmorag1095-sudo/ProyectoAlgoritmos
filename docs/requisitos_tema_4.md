# Resumen del Tema 4

## Tema

Algoritmos recursivos.

## Problema

Juego de la vida - Ecosistema costarricense.

El programa simula generaciones de microorganismos en una reserva natural costarricense usando las reglas clasicas del Juego de la Vida de Conway.

## Reglas de sobrevivencia

- Cada celda tiene 8 vecinos: horizontal, vertical y diagonal.
- Un microorganismo muere de soledad si tiene menos de 2 vecinos vivos.
- Un microorganismo muere de asfixia si tiene mas de 3 vecinos vivos.
- Una celda vacia con exactamente 3 vecinos vivos genera un nacimiento.
- Con 2 o 3 vecinos vivos, el microorganismo sobrevive a la siguiente generacion.

## Requisito tecnico obligatorio

El conteo de vecinos debe hacerse con recursividad.

No se permite usar `for` o `while` para recorrer las 8 posiciones vecinas. El recorrido de vecinos debe hacerse con una funcion que se llama a si misma.

## Preguntas probables en la defensa

- Donde esta el caso base de la recursion?
- Que pasa cuando el indice llega a 8?
- Por que no se cuenta la celda central?
- Que regla aplica cuando una celda viva tiene menos de 2 vecinos?
- Que regla aplica cuando una celda vacia tiene exactamente 3 vecinos?

## Respuestas cortas

- El caso base esta en `indice == DIRECCIONES_VECINOS.length`.
- Cuando el indice llega a 8, ya se revisaron los 8 vecinos y se retorna 0.
- No se cuenta la celda central porque el arreglo de direcciones no contiene la posicion `{0, 0}`.
- Si una celda viva tiene menos de 2 vecinos, muere por soledad.
- Si una celda vacia tiene exactamente 3 vecinos vivos, nace un nuevo microorganismo.
