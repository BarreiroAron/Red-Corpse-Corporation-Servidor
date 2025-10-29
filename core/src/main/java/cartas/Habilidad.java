package cartas;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import Entidades.Entidad;
import Entidades.Jugador;
import Pantallas.JuegoPantalla;
import Utiles.Render;
import juegos.ControladorDeJuego;
import juegos.HabilidadActiva;
import juegos.Juego;

public enum Habilidad {
	
    MODIFICAR_PUNTOS { //modifica puntos en general
        @Override
        public void ejecutar(Carta carta, Entidad jugador, Entidad rival, ControladorDeJuego controlador) {
           // jugador.modificarPuntos(carta.getPuntosDisminuidos());
           // rival.modificarPuntos(carta.getPuntosAumentadosRival());
        }
    },
    BLOQUEAR_ROBO { //Para efectos generales (seguramente "Not today" va a usarla si o si) y redento
        @Override
        public void ejecutar(Carta carta, Entidad jugador, Entidad rival, ControladorDeJuego controlador) {
        	if (controlador instanceof Juego) {
                Juego juego = (Juego) controlador;

                if (carta.getEnemigoDeterminadoEnum() == cartas.EnemigoDeterminado.GLOBAL) {
                    juego.activarBloqueoRobarGlobal(4, "Redento: no puedes robar del mazo");
                } else {
                    juego.activarBloqueoRobar(rival, 4, "Redento: no puedes robar del mazo");
                }
            }
        }
    },
    
    BLOQUEAR_EFECTO { //Sirve para efecto de Not today
        public void ejecutar(Carta carta, Entidad jugador, Entidad rival, ControladorDeJuego controlador) {
            
        }
    },
    
    CAMBIO_DIRECCION { //Sirve para efecto de Cambio de ronda
        @Override
        public void ejecutar(Carta carta, Entidad jugador, Entidad rival, ControladorDeJuego controlador) {
            controlador.cambiarDireccion();
        }
    },
    
    VER_CARTAS_SIGUIENTES {
        @Override
        public void ejecutar(Carta carta, Entidad jugador, Entidad rival, ControladorDeJuego controlador) {
            if (controlador instanceof Juego juego) {
                juego.activarVerCartas();
            }
        }
    },
    
    MEZCLAR_MAZO() { //Sirve para efectos de King Dice
        @Override
        public void ejecutar(Carta carta, Entidad jugador, Entidad rival, ControladorDeJuego controlador) {
        	ArrayList<Carta> cartasMalas= controlador.getListaPorTipoCartas(TipoDeCarta.MALA,controlador.getMesa());
        	if(cartasMalas.size()>1) {
        		controlador.pasarCartas(cartasMalas, controlador.getMazo());
        		System.out.println("Se salio de pasar cartas a eliminar por lista cartas");
        		controlador.getMesa().removeAll(cartasMalas);
        	}
        	controlador.mezclarMazo();
        }
    },

    
    
    
    REINICIAR_PARTIDA() { //Sirve para efectos de Company
        public void ejecutar(Carta carta, Entidad jugador, Entidad rival, ControladorDeJuego controlador) {
        	controlador.marcarReinicio();
        }
    },
    
    SALTEAR_TURNO() { //sirve para efectos de Saltamontes
        public void ejecutar(Carta carta, Entidad jugador, Entidad rival, ControladorDeJuego controlador) {
        	controlador.siguienteJugador();
        	System.out.println("Salteamos turno");
        }
    },
    
    INTERCAMBIO_PUNTOS() { //sirve para efectos de Chester
        public void ejecutar(Carta carta, Entidad jugador, Entidad rival, ControladorDeJuego controlador) {
            controlador.intercambiarPuntos(jugador, rival);
            System.out.println("Cambiamos puntos con el de adelante");
            
        }
    },
    
    SIN_CARTAS_EN_LA_MANO() { //sirve para efectos de Inanicion
        public void ejecutar(Carta carta, Entidad jugador, Entidad rival, ControladorDeJuego controlador) {
            
        }
    },
    
    COLERA() { //sirve para efectos de COLERA
        public void ejecutar(Carta carta, Entidad jugador, Entidad rival, ControladorDeJuego controlador) {
            controlador.activarColera(jugador);
            System.out.println("Se activo colera a "+ jugador.getNombre());
            jugador.modificarPuntos(-100, false);
        }
    },
    
    APARICION_ALEATORIA() {
        @Override
        public void ejecutar(Carta carta, Entidad jugador, Entidad rival, ControladorDeJuego controlador) {
            if (!(controlador instanceof Juego juego)) return;
            
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
            	@Override
            	public void run() {
            	mostrarVentana("IMSCARED.png", 400, 300, 100, 100);	
            	mostrarVentana("IMSCAREDDOS.png", 500, 400, 250, 200);
            	mostrarVentana("IMSCAREDTRES.png", 600, 700, 300, 300);
            	} 
            });
        }

        private void mostrarVentana(String archivo, int ancho, int alto, int x, int y) {
            javax.swing.JFrame frame = new javax.swing.JFrame("IM HERE!");
            frame.setSize(ancho, alto);
            frame.setLocation(x, y);
            frame.setAlwaysOnTop(true);
            frame.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
            java.net.URL url = getClass().getResource("/" + archivo);
            javax.swing.ImageIcon icono = new javax.swing.ImageIcon(url);
            javax.swing.JLabel label = new javax.swing.JLabel(icono);
            frame.add(label);
            frame.setVisible(true);
        }

    },


    
    ROBAR_CARTA() { //Esta carta esta hecha para el pecado de la codicia
    	public void ejecutar(Carta carta, Entidad jugador, Entidad rival, ControladorDeJuego controlador) {
    		if (controlador instanceof Juego juego) {
                juego.robarCartaMazo(jugador, true); // ignora bloqueos de robo (excepción)
            }
    	}
    },
    
    ESTRENIMIENTO() { //Carta para estrenimiento

    	public void ejecutar(Carta carta, Entidad jugador, Entidad rival, ControladorDeJuego controlador) {
    		//controlador.robarCartasMalas(rival);
    		if(controlador instanceof Juego juego) {
    			juego.activarEstrenimiento(rival);
    		}
    	}
    },
    
    HAMBRE() { //Carta que se va a usar para la carta de HAMBRE CONTENIDA
    	public void ejecutar(Carta carta, Entidad jugador, Entidad rival, ControladorDeJuego controlador) {
    		controlador.modificarPuntosGlobal(jugador,-30, false);
    		controlador.activarRobarMazoAEleccion(jugador);
    	}
    }, 
    
    MIMICO() {
        public void ejecutar(Carta carta, Entidad jugador, Entidad rival, ControladorDeJuego controlador) {
            if (controlador instanceof Juego juego) {
                juego.activarMimicoPendiente(jugador);
                System.out.println("Mímico jugado: se activó efecto pendiente de próximo robo.");
            }
        }
    },
    
    VER_PUNTOS_RIVAL() {
    	public void ejecutar(Carta carta, Entidad jugador, Entidad rival, ControladorDeJuego controlador) {
    		if (controlador instanceof Juego juego) {
    			juego.activarVerPuntos();
    		}
    		
    	}
    },
    
    TIRAR_CARTA_ALEATOREA { //modifica puntos en general
        public void ejecutar(Carta carta, Entidad jugador, Entidad rival, ControladorDeJuego controlador) {
        	if (controlador instanceof Juego) {
        		Juego juego = (Juego) controlador;
        		
        		juego.activarJugarCartaAleatorea(rival,3, "Redento: no puedes robar del mazo");
        	}
        }
    },
    BLOQUEO() {
        public void ejecutar(Carta carta, Entidad jugador, Entidad rival, ControladorDeJuego controlador) {
            if (controlador instanceof Juego juego) {
                // activa un efecto temporal de bloqueo
                juego.habilidadesActivas.add(HabilidadActiva.bloqueoActivo(jugador));
                System.out.println("Carta BLOQUEO jugada → bloqueará el próximo efecto pendiente.");
            }
        }
    };
	
    public abstract void ejecutar(Carta carta, Entidad jugador, Entidad rival, ControladorDeJuego controlador);

}
