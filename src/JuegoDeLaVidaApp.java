import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

public class JuegoDeLaVidaApp extends JFrame {
    private static final int MIN_TAMANO = 4;
    private static final int MAX_TAMANO = 8;

    private static final int[][] DIRECCIONES_VECINOS = {
            {-1, -1}, {-1, 0}, {-1, 1},
            {0, -1},           {0, 1},
            {1, -1},  {1, 0},  {1, 1}
    };

    private static final String[] RESERVAS = {
            "Parque Nacional Corcovado",
            "Reserva Biologica Monteverde",
            "Parque Nacional Tortuguero",
            "Parque Nacional Rincon de la Vieja"
    };

    private static final String[] SECTORES = {
            "Sector Sirena", "Sector La Leona", "Sector Los Patos", "Sector San Pedrillo",
            "Sendero Nuboso", "Sendero Pantanoso", "Bosque Eterno", "Valle Escondido",
            "Canal Principal", "Laguna Norte", "Bosque Caribe", "Playa Verde",
            "Sector Pailas", "Sector Santa Maria", "Sendero Catarata", "Bosque Seco"
    };

    private enum EstadoVisual {
        VACIA,
        VIVA_INICIAL,
        SOBREVIVIENTE,
        NACIMIENTO
    }

    private boolean[][] matrizActual;
    private EstadoVisual[][] estadosVisuales;
    private JButton[][] botonesCeldas;
    private JPanel panelMatriz;
    private JTextArea areaResumen;
    private JLabel etiquetaGeneracion;
    private JLabel etiquetaVivos;
    private JSpinner spinnerTamano;
    private JSpinner spinnerGeneraciones;
    private JComboBox<String> comboReserva;
    private int generacionActual;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JuegoDeLaVidaApp ventana = new JuegoDeLaVidaApp();
            ventana.setVisible(true);
        });
    }

    public JuegoDeLaVidaApp() {
        setTitle("Tema 4 - Juego de la Vida Recursivo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(960, 640));
        inicializarComponentes();
        crearMatrizNueva();
        setLocationRelativeTo(null);
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(12, 12));

        JPanel panelControles = new JPanel(new GridLayout(2, 1, 8, 8));
        panelControles.setBorder(BorderFactory.createEmptyBorder(12, 12, 0, 12));

        JPanel filaUno = new JPanel();
        spinnerTamano = new JSpinner(new SpinnerNumberModel(4, MIN_TAMANO, MAX_TAMANO, 1));
        spinnerGeneraciones = new JSpinner(new SpinnerNumberModel(1, 1, 50, 1));
        comboReserva = new JComboBox<>(RESERVAS);

        filaUno.add(new JLabel("Tamano matriz:"));
        filaUno.add(spinnerTamano);
        filaUno.add(new JLabel("Generaciones:"));
        filaUno.add(spinnerGeneraciones);
        filaUno.add(new JLabel("Reserva:"));
        filaUno.add(comboReserva);

        JPanel filaDos = new JPanel();
        JButton botonCrear = new JButton("Crear matriz");
        JButton botonSiguiente = new JButton("Siguiente generacion");
        JButton botonSimular = new JButton("Simular cantidad");
        JButton botonLimpiar = new JButton("Limpiar");

        botonCrear.addActionListener(evento -> crearMatrizNueva());
        botonSiguiente.addActionListener(evento -> avanzarUnaGeneracion());
        botonSimular.addActionListener(evento -> simularCantidadIndicada());
        botonLimpiar.addActionListener(evento -> limpiarMatriz());
        comboReserva.addActionListener(evento -> actualizarTodaLaMatriz());

        filaDos.add(botonCrear);
        filaDos.add(botonSiguiente);
        filaDos.add(botonSimular);
        filaDos.add(botonLimpiar);

        panelControles.add(filaUno);
        panelControles.add(filaDos);
        add(panelControles, BorderLayout.NORTH);

        panelMatriz = new JPanel();
        panelMatriz.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 6));
        add(panelMatriz, BorderLayout.CENTER);

        JPanel panelDerecho = new JPanel(new BorderLayout(8, 8));
        panelDerecho.setBorder(BorderFactory.createEmptyBorder(12, 6, 12, 12));

        JPanel panelEstado = new JPanel(new GridLayout(2, 1, 4, 4));
        etiquetaGeneracion = new JLabel("Generacion: 0");
        etiquetaVivos = new JLabel("Microorganismos vivos: 0");
        panelEstado.add(etiquetaGeneracion);
        panelEstado.add(etiquetaVivos);

        areaResumen = new JTextArea();
        areaResumen.setEditable(false);
        areaResumen.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        areaResumen.setLineWrap(true);
        areaResumen.setWrapStyleWord(true);

        JScrollPane scrollResumen = new JScrollPane(areaResumen);
        scrollResumen.setPreferredSize(new Dimension(390, 520));

        panelDerecho.add(panelEstado, BorderLayout.NORTH);
        panelDerecho.add(scrollResumen, BorderLayout.CENTER);
        add(panelDerecho, BorderLayout.EAST);
    }

    private void crearMatrizNueva() {
        int tamano = (Integer) spinnerTamano.getValue();
        matrizActual = new boolean[tamano][tamano];
        estadosVisuales = new EstadoVisual[tamano][tamano];
        botonesCeldas = new JButton[tamano][tamano];
        generacionActual = 0;

        llenarEstadosVisuales(EstadoVisual.VACIA);
        construirPanelMatriz();
        areaResumen.setText("");
        areaResumen.append("Tema 4 - Algoritmos recursivos\n");
        areaResumen.append("Problema: Juego de la vida - Ecosistema costarricense\n");
        areaResumen.append("Reserva seleccionada: " + comboReserva.getSelectedItem() + "\n");
        areaResumen.append("Instruccion: haga clic en las celdas para colocar microorganismos iniciales.\n\n");
        actualizarEtiquetas();
    }

    private void construirPanelMatriz() {
        int tamano = matrizActual.length;
        panelMatriz.removeAll();
        panelMatriz.setLayout(new GridLayout(tamano, tamano, 6, 6));

        for (int fila = 0; fila < tamano; fila++) {
            for (int columna = 0; columna < tamano; columna++) {
                JButton boton = new JButton();
                boton.setHorizontalAlignment(SwingConstants.CENTER);
                boton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));

                final int filaSeleccionada = fila;
                final int columnaSeleccionada = columna;
                boton.addActionListener(evento -> alternarMicroorganismo(filaSeleccionada, columnaSeleccionada));

                botonesCeldas[fila][columna] = boton;
                panelMatriz.add(boton);
            }
        }

        actualizarTodaLaMatriz();
        panelMatriz.revalidate();
        panelMatriz.repaint();
    }

    private void alternarMicroorganismo(int fila, int columna) {
        if (generacionActual > 0) {
            int respuesta = JOptionPane.showConfirmDialog(
                    this,
                    "Cambiar una celda reinicia el experimento desde la generacion actual.",
                    "Reiniciar experimento",
                    JOptionPane.YES_NO_OPTION
            );

            if (respuesta != JOptionPane.YES_OPTION) {
                return;
            }

            generacionActual = 0;
            marcarVivosComoIniciales();
            areaResumen.append("\nSe reinicio el experimento para editar la matriz.\n\n");
        }

        matrizActual[fila][columna] = !matrizActual[fila][columna];
        estadosVisuales[fila][columna] = matrizActual[fila][columna]
                ? EstadoVisual.VIVA_INICIAL
                : EstadoVisual.VACIA;

        actualizarCelda(fila, columna);
        actualizarEtiquetas();
    }

    private void limpiarMatriz() {
        for (int fila = 0; fila < matrizActual.length; fila++) {
            for (int columna = 0; columna < matrizActual.length; columna++) {
                matrizActual[fila][columna] = false;
                estadosVisuales[fila][columna] = EstadoVisual.VACIA;
            }
        }

        generacionActual = 0;
        areaResumen.setText("Matriz limpia. Coloque los microorganismos iniciales.\n\n");
        actualizarTodaLaMatriz();
        actualizarEtiquetas();
    }

    private void simularCantidadIndicada() {
        int generaciones = (Integer) spinnerGeneraciones.getValue();

        for (int contador = 0; contador < generaciones; contador++) {
            avanzarUnaGeneracion();
        }
    }

    private void avanzarUnaGeneracion() {
        ResultadoGeneracion resultado = calcularSiguienteGeneracion(matrizActual);

        matrizActual = resultado.siguiente;
        estadosVisuales = resultado.estadosVisuales;
        generacionActual++;

        agregarResumenGeneracion(resultado);
        actualizarTodaLaMatriz();
        actualizarEtiquetas();
    }

    private ResultadoGeneracion calcularSiguienteGeneracion(boolean[][] matriz) {
        int tamano = matriz.length;
        ResultadoGeneracion resultado = new ResultadoGeneracion(tamano);

        for (int fila = 0; fila < tamano; fila++) {
            for (int columna = 0; columna < tamano; columna++) {
                int vecinosVivos = contarVecinosRecursivo(matriz, fila, columna, 0);
                boolean estaViva = matriz[fila][columna];

                if (estaViva && (vecinosVivos == 2 || vecinosVivos == 3)) {
                    resultado.siguiente[fila][columna] = true;
                    resultado.estadosVisuales[fila][columna] = EstadoVisual.SOBREVIVIENTE;
                    resultado.sobrevivientes.add(formatoPosicion(fila, columna));
                } else if (!estaViva && vecinosVivos == 3) {
                    resultado.siguiente[fila][columna] = true;
                    resultado.estadosVisuales[fila][columna] = EstadoVisual.NACIMIENTO;
                    resultado.nacimientos.add(formatoPosicion(fila, columna));
                } else if (estaViva) {
                    resultado.siguiente[fila][columna] = false;
                    resultado.estadosVisuales[fila][columna] = EstadoVisual.VACIA;

                    if (vecinosVivos < 2) {
                        resultado.muertes.add(formatoPosicion(fila, columna) + " soledad");
                    } else {
                        resultado.muertes.add(formatoPosicion(fila, columna) + " asfixia");
                    }
                } else {
                    resultado.siguiente[fila][columna] = false;
                    resultado.estadosVisuales[fila][columna] = EstadoVisual.VACIA;
                }
            }
        }

        return resultado;
    }

    private int contarVecinosRecursivo(boolean[][] matriz, int fila, int columna, int indice) {
        if (indice == DIRECCIONES_VECINOS.length) {
            return 0;
        }

        int nuevaFila = fila + DIRECCIONES_VECINOS[indice][0];
        int nuevaColumna = columna + DIRECCIONES_VECINOS[indice][1];
        int vecinoActual = 0;

        if (estaDentroDeLaMatriz(matriz, nuevaFila, nuevaColumna) && matriz[nuevaFila][nuevaColumna]) {
            vecinoActual = 1;
        }

        return vecinoActual + contarVecinosRecursivo(matriz, fila, columna, indice + 1);
    }

    private boolean estaDentroDeLaMatriz(boolean[][] matriz, int fila, int columna) {
        return fila >= 0 && fila < matriz.length && columna >= 0 && columna < matriz.length;
    }

    private void agregarResumenGeneracion(ResultadoGeneracion resultado) {
        areaResumen.append("Generacion " + generacionActual + " -> " + (generacionActual + 1) + "\n");
        areaResumen.append("Matriz resultante: S=sobrevive, N=nace, .=vacia\n");
        areaResumen.append(construirTextoMatriz(resultado.estadosVisuales));
        areaResumen.append("Sobrevivientes (" + resultado.sobrevivientes.size() + "): "
                + formatearLista(resultado.sobrevivientes) + "\n");
        areaResumen.append("Nacimientos (" + resultado.nacimientos.size() + "): "
                + formatearLista(resultado.nacimientos) + "\n");
        areaResumen.append("Muertes (" + resultado.muertes.size() + "): "
                + formatearLista(resultado.muertes) + "\n");
        areaResumen.append("Total vivos al final: "
                + (resultado.sobrevivientes.size() + resultado.nacimientos.size()) + "\n\n");
        areaResumen.setCaretPosition(areaResumen.getDocument().getLength());
    }

    private String construirTextoMatriz(EstadoVisual[][] estados) {
        StringBuilder texto = new StringBuilder();

        for (int fila = 0; fila < estados.length; fila++) {
            for (int columna = 0; columna < estados.length; columna++) {
                texto.append(simboloResumen(estados[fila][columna])).append(' ');
            }
            texto.append('\n');
        }

        return texto.toString();
    }

    private String simboloResumen(EstadoVisual estado) {
        if (estado == EstadoVisual.SOBREVIVIENTE || estado == EstadoVisual.VIVA_INICIAL) {
            return "S";
        }

        if (estado == EstadoVisual.NACIMIENTO) {
            return "N";
        }

        return ".";
    }

    private String formatearLista(List<String> valores) {
        if (valores.isEmpty()) {
            return "ninguno";
        }

        return String.join(", ", valores);
    }

    private void actualizarTodaLaMatriz() {
        if (botonesCeldas == null) {
            return;
        }

        for (int fila = 0; fila < botonesCeldas.length; fila++) {
            for (int columna = 0; columna < botonesCeldas.length; columna++) {
                actualizarCelda(fila, columna);
            }
        }
    }

    private void actualizarCelda(int fila, int columna) {
        JButton boton = botonesCeldas[fila][columna];
        EstadoVisual estado = estadosVisuales[fila][columna];

        boton.setText("<html><center>Z" + (fila + 1) + "-" + (columna + 1)
                + "<br>" + textoEstado(estado) + "</center></html>");
        boton.setBackground(colorEstado(estado));
        boton.setOpaque(true);
        boton.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        boton.setToolTipText(comboReserva.getSelectedItem() + " - "
                + nombreZona(fila, columna) + " - posicion (" + fila + "," + columna + ")");
    }

    private String textoEstado(EstadoVisual estado) {
        if (estado == EstadoVisual.NACIMIENTO) {
            return "NACE";
        }

        if (estado == EstadoVisual.SOBREVIVIENTE) {
            return "VIVE";
        }

        if (estado == EstadoVisual.VIVA_INICIAL) {
            return "INICIO";
        }

        return "VACIA";
    }

    private Color colorEstado(EstadoVisual estado) {
        if (estado == EstadoVisual.NACIMIENTO) {
            return new Color(142, 202, 142);
        }

        if (estado == EstadoVisual.SOBREVIVIENTE) {
            return new Color(122, 170, 214);
        }

        if (estado == EstadoVisual.VIVA_INICIAL) {
            return new Color(181, 211, 238);
        }

        return Color.WHITE;
    }

    private String nombreZona(int fila, int columna) {
        int indice = (fila * matrizActual.length + columna) % SECTORES.length;
        return SECTORES[indice];
    }

    private void actualizarEtiquetas() {
        etiquetaGeneracion.setText("Generacion: " + generacionActual);
        etiquetaVivos.setText("Microorganismos vivos: " + contarVivosActuales());
    }

    private int contarVivosActuales() {
        int vivos = 0;

        for (int fila = 0; fila < matrizActual.length; fila++) {
            for (int columna = 0; columna < matrizActual.length; columna++) {
                if (matrizActual[fila][columna]) {
                    vivos++;
                }
            }
        }

        return vivos;
    }

    private void llenarEstadosVisuales(EstadoVisual estado) {
        for (int fila = 0; fila < estadosVisuales.length; fila++) {
            for (int columna = 0; columna < estadosVisuales.length; columna++) {
                estadosVisuales[fila][columna] = estado;
            }
        }
    }

    private void marcarVivosComoIniciales() {
        for (int fila = 0; fila < matrizActual.length; fila++) {
            for (int columna = 0; columna < matrizActual.length; columna++) {
                estadosVisuales[fila][columna] = matrizActual[fila][columna]
                        ? EstadoVisual.VIVA_INICIAL
                        : EstadoVisual.VACIA;
            }
        }
    }

    private String formatoPosicion(int fila, int columna) {
        return "(" + fila + "," + columna + ")";
    }

    private static class ResultadoGeneracion {
        private final boolean[][] siguiente;
        private final EstadoVisual[][] estadosVisuales;
        private final List<String> sobrevivientes;
        private final List<String> nacimientos;
        private final List<String> muertes;

        private ResultadoGeneracion(int tamano) {
            siguiente = new boolean[tamano][tamano];
            estadosVisuales = new EstadoVisual[tamano][tamano];
            sobrevivientes = new ArrayList<>();
            nacimientos = new ArrayList<>();
            muertes = new ArrayList<>();
        }
    }
}
