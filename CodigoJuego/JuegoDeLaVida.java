import javax.swing.JOptionPane;

public class JuegoDeLaVida
{
	public static void main(String[] args)
	{
		int opcion = 0;
		int tamanoMatriz = 0;
		int generaciones = 0;
		int generacionActual = 0;
		String[][] matriz = null;
		boolean datosIngresados = false;
		String textoMatriz = "Generacion " + generacionActual + " - Estado actual del ecosistema:\n";
		
		//-----------------------------------------------------------------------------------------------------------------------------------
		do
		{
			opcion = Integer.parseInt(JOptionPane.showInputDialog(
			"Juego de la Vida - Parque Nacional Corcovado\n\n" +
			"1. Crear ecosistema inicial\n" +
			"2. Mostrar estado del ecosistema\n" +
			"3. Revisar vecinos vivos de una zona\n" +
			"4. Simular generaciones del ecosistema\n" +
			"5. Salir\n\n" +
			"Seleccione una opcion:"));
			
			if(opcion == 1)
			{
				//-----------------------------------------------------------------------------------------------------------------------------------
				do
				{
					tamanoMatriz = Integer.parseInt(JOptionPane.showInputDialog("Ingrese el tamaño del terreno (4 a 8 zonas por lado):"));
					
					if(tamanoMatriz < 4 || tamanoMatriz > 8)
					{
						JOptionPane.showMessageDialog(null,"El terreno debe estar entre 4x4 y 8x8 zonas.");
					}//Fin del if
					
				}while(tamanoMatriz < 4 || tamanoMatriz > 8);
				
				//Se crea la matriz una vez se tiene el tamano del terreno
				matriz = new String[tamanoMatriz][tamanoMatriz];
				
				//Se ingresa la cantidad de generaciones que tendra el ecosistema
				do
				{
					generaciones = Integer.parseInt(JOptionPane.showInputDialog("Ingrese la cantidad de generaciones del ecosistema:"));
					
					if(generaciones < 1)
					{
						JOptionPane.showMessageDialog(null, "La cantidad de generaciones debe ser mayor a 0.");
					}//Fin del if
				}while(generaciones < 1);
			
				for(int fila = 0; fila < matriz.length; fila++)
				{
					for(int columna = 0; columna < matriz[fila].length; columna++)
					{
						matriz[fila][columna] = "-";
					}//Fin del for
				}//Fin del for
				//-----------------------------------------------------------------------------------------------------------------------------------
				//Aqui solicitaremos cuantos microorganismos vivos se colocaran
				int cantidadMicroorganismos = 0;
				
				do
				{
					cantidadMicroorganismos = Integer.parseInt(JOptionPane.showInputDialog("Ingrese la cantidad de microorganismos iniciales en el terreno:"));
					
					if(cantidadMicroorganismos < 1 || cantidadMicroorganismos > tamanoMatriz * tamanoMatriz)
					{
						JOptionPane.showMessageDialog(null,"La cantidad debe estar entre 1 y " + (tamanoMatriz * tamanoMatriz) + " zonas disponibles.");
					}//Fin del if
				}while(cantidadMicroorganismos < 1 || cantidadMicroorganismos > tamanoMatriz * tamanoMatriz);
				//-----------------------------------------------------------------------------------------------------------------------------------
				//Colocacion de cada microorganismo en una posicion 
				for(int contador = 1; contador <= cantidadMicroorganismos; contador++)
				{
					boolean posicionValida = false; 
					
					while(!posicionValida)
					{
						int fila = Integer.parseInt(JOptionPane.showInputDialog("Microorganismo " + contador + "\nIngrese la fila de la zona (1 a " + tamanoMatriz + "):")) - 1;
						
						int columna = Integer.parseInt(JOptionPane.showInputDialog("Microorganismo " + contador + "\nIngrese la columna de la zona (1 a " + tamanoMatriz + "):")) - 1;
				
						//Validamos que la posicion exista dentro de la matriz 
						if(fila < 0 || fila >= matriz.length || columna < 0 || columna >= matriz[fila].length)
						{
							JOptionPane.showMessageDialog(null, "La zona esta fuera del terreno.");
						}//Fin del if
						//Validamos que una posicion ya ocupada no se repita
						else if(matriz[fila][columna].equals("X"))
						{
							JOptionPane.showMessageDialog(null, "Ya existe un microorganismo en esa zona.");
						}else
						{
							matriz[fila][columna] = "X";
							posicionValida = true;
						}//Fin del else
					}//Fin del while 
				}//Fin del for
				
				datosIngresados = true;
				generacionActual = 0;
				JOptionPane.showMessageDialog(null, "Ecosistema inicial creado correctamente.");
			}//Fin del if
			else if(opcion == 2)
			{
				if(!datosIngresados || matriz == null)
				{
					JOptionPane.showMessageDialog(null, "No existe ningun ecosistema creado. Primero use la opcion 1.");
				}else
				{
					textoMatriz += "Reserva: Parque Nacional Corcovado\n";
					textoMatriz += "Tamano del terreno: " + tamanoMatriz + "x" + tamanoMatriz + " zonas\n";
					textoMatriz += "Generaciones a simular: " + generaciones + "\n\n";
					
					//Forma el texto de la matriz
					for(int fila = 0; fila < matriz.length; fila++)
					{
						for(int columna = 0; columna < matriz[fila].length; columna++)
						{
							textoMatriz += matriz[fila][columna] + " ";
						}//Fin del for
						textoMatriz += "\n";
					}//Fin del for
					
					JOptionPane.showMessageDialog(null, textoMatriz);
				}//Fin del else
			}//Fin del elseif
			else if(opcion == 3)
			{
				if(!datosIngresados || matriz == null)
				{
					JOptionPane.showMessageDialog(null, "No existe ningun ecosistema creado. Primero use la opcion 1.");
				}else
				{
					int filaPrueba = Integer.parseInt(JOptionPane.showInputDialog("Ingrese la fila de la zona (1 a " + tamanoMatriz + "):")) - 1;
					
					int columnaPrueba = Integer.parseInt(JOptionPane.showInputDialog("Ingrese la columna de la zona (1 a " + tamanoMatriz + "):")) - 1;
					
					//Valida que la celda exista
					if(filaPrueba < 0 || filaPrueba >= matriz.length || columnaPrueba < 0 || columnaPrueba >= matriz[filaPrueba].length)
					{
						JOptionPane.showMessageDialog(null, "La zona esta fuera del terreno.");
					}//Fin del if 
					else
					{
						//Cuenta vecinos con recursividad
						int vecinos = contarVecinosVivos(matriz, filaPrueba, columnaPrueba, 0);
						
						JOptionPane.showMessageDialog(null,"La zona (" + (filaPrueba + 1) + "," + (columnaPrueba + 1) + ") tiene " + vecinos + " vecinos vivos alrededor.");
					}//Fin del else
				}//Fin del else
			}//Fin del elseif
			else if(opcion == 4)
			{
				if(!datosIngresados || matriz == null)
				{
					JOptionPane.showMessageDialog(null, "No existe ningun ecosistema creado. Primero use la opcion 1.");
				}else
				{
					//Simula las generaciones y actualiza la matriz
					matriz = simularGeneraciones(matriz, generaciones, generacionActual);
					generacionActual += generaciones;
				}//Fin del else
			}//Fin del elseif
			else if(opcion == 5)
			{
				JOptionPane.showMessageDialog(null, "Programa finalizado.");
			}//Fin del elseif
			else
			{
				JOptionPane.showMessageDialog(null, "Opcion invalida.");
			}//Fin del else
		}while(opcion != 5);
	}//Fin del metodo main 
//-----------------------------------------------------------------------------------------------------------------------------------
	static String[][] simularGeneraciones(String[][] matriz, int generaciones, int generacionActual)
	{
		for(int numeroGeneracion = 1; numeroGeneracion <= generaciones; numeroGeneracion++)
		{
			String[][] nuevaMatriz = new String[matriz.length][matriz[0].length];
			
			int nacimientos = 0;
			int sobrevivientes = 0;
			int muertes = 0;
			int totalVivos = 0;
			
			String detalleNacimientos = "";
			String detalleSobrevivientes = "";
			String detalleMuertes = "";
			
			//Analiza cada zona del ecosistema
			for(int fila = 0; fila < matriz.length; fila++)
			{
				for(int columna = 0; columna < matriz[fila].length; columna++)
				{
					int vecinos = contarVecinosVivos(matriz, fila, columna, 0);
					
					if(matriz[fila][columna].equals("X"))
					{
						if(vecinos < 2 || vecinos > 3)
						{
							nuevaMatriz[fila][columna] = "-";
							muertes++;
							detalleMuertes += "(" + (fila + 1) + "," + (columna + 1) + ") ";
						}else
						{
							nuevaMatriz[fila][columna] = "X";
							sobrevivientes++;
							totalVivos++;
							detalleSobrevivientes += "(" + (fila + 1) + "," + (columna + 1) + ") ";
						}//Fin del else
					}else
					{
						if(vecinos == 3)
						{
							nuevaMatriz[fila][columna] = "X";
							nacimientos++;
							totalVivos++;
							detalleNacimientos += "(" + (fila + 1) + "," + (columna + 1) + ") ";
						}else
						{
							nuevaMatriz[fila][columna] = "-";
						}//Fin del else
					}//Fin del else
				}//Fin del for
			}//Fin del for
			
			String textoGeneracion = "Generacion " + (generacionActual + numeroGeneracion) + " - Parque Nacional Corcovado\n\n";
			textoGeneracion += "Estado del ecosistema:\n";
			
			//Forma el texto de la nueva matriz
			for(int fila = 0; fila < nuevaMatriz.length; fila++)
			{
				for(int columna = 0; columna < nuevaMatriz[fila].length; columna++)
				{
					textoGeneracion += nuevaMatriz[fila][columna] + " ";
				}//Fin del for
				textoGeneracion += "\n";
			}//Fin del for
			
			if(detalleNacimientos.equals(""))
			{
				detalleNacimientos = "Ninguno";
			}//Fin del if
			
			if(detalleSobrevivientes.equals(""))
			{
				detalleSobrevivientes = "Ninguno";
			}//Fin del if
			
			if(detalleMuertes.equals(""))
			{
				detalleMuertes = "Ninguno";
			}//Fin del if
			
			textoGeneracion += "\nMicroorganismos que sobreviven: " + sobrevivientes + "\n";
			textoGeneracion += "Detalle: " + detalleSobrevivientes + "\n\n";
			textoGeneracion += "Microorganismos que nacen: " + nacimientos + "\n";
			textoGeneracion += "Detalle: " + detalleNacimientos + "\n\n";
			textoGeneracion += "Microorganismos que mueren: " + muertes + "\n";
			textoGeneracion += "Detalle: " + detalleMuertes + "\n\n";
			textoGeneracion += "Total vivos al final de la generacion: " + totalVivos;
			
			JOptionPane.showMessageDialog(null, textoGeneracion);
			
			//La nueva matriz pasa a ser la actual
			matriz = nuevaMatriz;
		}//Fin del for
		
		return matriz;
	}//Fin del metodo simularGeneraciones
//-----------------------------------------------------------------------------------------------------------------------------------
	static int contarVecinosVivos(String[][] matriz, int fila, int columna, int indice)
	{
		//Caso base: ya se revisaron los 8 vecinos
		if(indice == 8)
		{
			return 0;
		}//Fin del if
		
		//Movimientos para revisar vecinos alrededor de la celda
		int[] movimientosFila = {-1, -1, -1, 0, 0, 1, 1, 1};
		int[] movimientosColumna = {-1, 0, 1, -1, 1, -1, 0, 1};
		
		//Calcula la posicion del vecino actual
		int filaVecina = fila + movimientosFila[indice];
		int columnaVecina = columna + movimientosColumna[indice];
		
		int vecinoActual = 0;
		
		//Revisa que el vecino este dentro de la matriz
		if(filaVecina >= 0 && filaVecina < matriz.length && columnaVecina >= 0 && columnaVecina < matriz[filaVecina].length)
		{
			//Si hay microorganismo vivo, cuenta 1
			if(matriz[filaVecina][columnaVecina].equals("X"))
			{
				vecinoActual = 1;
			}//Fin del if
		}//Fin del if
		
		//Llamada recursiva al siguiente vecino
		return vecinoActual + contarVecinosVivos(matriz, fila, columna, indice + 1);
	}//Fin del metodo contarVecinosVivos
}//Fin de la clase 
