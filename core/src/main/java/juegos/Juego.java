package juegos;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.audio.Sound;

import Entidades.Entidad;
import Entidades.Jugador;
import cartas.Carta;
import cartas.Habilidad;
import cartas.TipoDeCarta;
import cartasEspeciales.IMHERE;
import cartasEspeciales.Inanicion;
import cartasMalas.PecadoDeLaCodicia;
import cartasMalas.Sonambulo;
import cartasNormales.CambioDeRonda;
import cartasNormales.Chester;
import cartasNormales.Colera;
import cartasNormales.Company;
import cartasNormales.Estrenimiento;
import cartasNormales.HambreContenida;
import cartasNormales.KingDice;
import cartasNormales.Mimico;
import cartasNormales.NotToday;
import cartasNormales.OjoQueTodoLoVe;
import cartasNormales.Redento;
import cartasNormales.Saltamontes;
import cartasNormales.Snake;
import cartasNormales.ThanksForPlaying;
import juegos.HabilidadActiva.Tipo;
import sonidos.SonidoAmbiental;
import sonidos.SonidoManager;

public class Juego implements ControladorDeJuego, TiempoListener {
	
	private int direccionRonda = 1;
	private int cantidadCartasMazo;
	
	private ArrayList<Carta> mazo;
	private ArrayList<Carta> mesa = new ArrayList<>();;
	private ArrayList<Entidad> jugadores;
	
	private int indiceMesa=0;
	private int indiceJugadorActual=0;
	private float tiempo =0.5f;
	private int rondas=0;
	
	Entidad jugadorPerdedor=null;
	
	HiloTiempoPartida hiloDeTiempo;
	
	private boolean debeReiniciar= false;
	
	private boolean partidaFinalizada= false;
	
	private boolean cartasDisponiblesMazo= true;

	//dislay para ejecucion de cartas
	private Carta cartaPendiente = null;
	private Entidad jugadorQueLaJugoPendiente = null;
	private int ticksPendientes = 0;

	
	
	public final ArrayList<HabilidadActiva> habilidadesActivas = new ArrayList<>();
	//intentar despues evitar esta variable
	int cantidadDeCartasASacar= 0;
	int indiceCartasASacar;
	
	private long ultimoCambioDeRonda = 0;
    private final long COOLDOWN_RONDA_MS = 3000;
	
	private ArrayList<Carta> cartasMostradas = new ArrayList<>();
	
    private Sound CartaTirada;
	
	public Juego( ArrayList<Entidad> Jugadores){
		this.jugadores= Jugadores;
		iniciarMazo();
		repartirCartas();
		this.hiloDeTiempo = new HiloTiempoPartida(this);
		this.hiloDeTiempo.setMinutos(tiempo);
		this.hiloDeTiempo.start();
		SonidoAmbiental VentiladorHilo = new SonidoAmbiental();
	}


	private void iniciarMazo() {
		System.out.println("Se creo mazo");
		mazo = new ArrayList<>();
				mazo.add(new Chester());
				mazo.add(new Colera());
				mazo.add(new Mimico());
				mazo.add(new Mimico());
				mazo.add(new Redento());

				for(int i = 0; i < 2; i++) {

				mazo.add(new Company());
				mazo.add(new HambreContenida());
				mazo.add(new Estrenimiento());
				mazo.add(new Snake());

				mazo.add(new PecadoDeLaCodicia());
				mazo.add(new Sonambulo());	

				}
				for(int i = 0; i < 4; i++) {

					mazo.add(new CambioDeRonda());
					mazo.add(new NotToday());
					mazo.add(new ThanksForPlaying());
					mazo.add(new KingDice());
					mazo.add(new OjoQueTodoLoVe());
					mazo.add(new Saltamontes());
				}
				
		cantidadCartasMazo = mazo.size();

	    Collections.shuffle(mazo);

	}
	
	public void actualizar(){
		actualizarReiniciarPartida();
		actualizarJugadorPerdedor();
		comprobarPartidaTerminada();
		actualizarCartasDisponiblesMazo();
		comprobarCondicionParaInanicion();
		
		tick();
	}
	
	public void tick() {
		if (cartaPendiente != null) {
		    ticksPendientes--;
		    if (ticksPendientes <= 0) {
		        ejecutarCartaPendiente();
		    }
		}
	}
	
	private void ejecutarCartaPendiente() {

		// si bloque_activo no essta se ejecuta
	    if (habilidadesActivas.stream().noneMatch(h -> h.getTipo() == HabilidadActiva.Tipo.BLOQUEO_ACTIVO)) {
	    	jugarCarta(cartaPendiente,jugadorQueLaJugoPendiente);
	    } else {
	        System.out.println("Carta bloqueada por efecto de Bloqueo.");
	    }

	    // limpia general
	    cartaPendiente = null;
	    jugadorQueLaJugoPendiente = null;
	    ticksPendientes = 0;
	    habilidadesActivas.removeIf(h -> h.getTipo() == HabilidadActiva.Tipo.BLOQUEO_ACTIVO);
	}
	
	private void comprobarCondicionParaInanicion() {
		for(int i=0; i< jugadores.size();i++) {
			Entidad jugadorActual = jugadores.get(i);
			if(jugadorActual.getMano().size()<=0) {
				habilidadesActivas.add(HabilidadActiva.inanicion(jugadorActual, 100, "Por cantidad de rondas con esta carta se multiplican tus puntos"));
				jugadorActual.agregarCarta(new Inanicion());
			}
			if(isHabilidadActivaEnJugador(HabilidadActiva.Tipo.INANICION, jugadorActual)&& jugadorActual.getMano().size()>=2) {
				System.out.println("Se termino el efecto inanicion" );
				habilidadesActivas.remove(buscarHabilidadActiva(HabilidadActiva.Tipo.INANICION));
				}
		}
	}
	
	private void limpiarInanicionSiCorresponde(Entidad jugador) {
	    if (jugador.getMano() == null || jugador.getMano().size() <= 1) return;
	    jugador.getMano().removeIf(c -> c instanceof Inanicion);
	}


	private Carta buscarCartaPorHabilidad(ArrayList<Carta> lista, Habilidad tipoHabilidad) {
	    for (Carta c : lista) {
	        if (c.getHabilidad() == tipoHabilidad) {
	            return c;
	        }
	    }
	    return null;
	}


	public HabilidadActiva getHabilidad( HabilidadActiva.Tipo TipoHabilidad ) {
		for (HabilidadActiva habilidad : habilidadesActivas) {
		      if (habilidad.getTipo() == TipoHabilidad) {
		            return habilidad;
		      }
		}
		return null;
	}
	
	public void activarColera(Entidad jugador) {
	    habilidadesActivas.add(HabilidadActiva.colera(jugador, 3, "Sos obligado a robar cartas por 3 turnos, si no te sale ninguna carta mala reduces 20 puntos"));
	    
	}
	
	public void activarBloqueoRobar(Entidad objetivo, int turnos, String descripcion) {
	    habilidadesActivas.add(HabilidadActiva.bloqueoRobarA(objetivo, turnos, descripcion));
	}
	public void activarBloqueoRobarGlobal(int turnos, String descripcion) {
	    habilidadesActivas.add(HabilidadActiva.bloqueoRobarGlobal(turnos, descripcion));
	}
	
	public void activarJugarCartaAleatorea(Entidad objetivo,int turnos, String descripcion) {
	    habilidadesActivas.add(HabilidadActiva.jugarCartaAleatorea(objetivo,turnos, descripcion));
	}

	@Override
	public void activarRobarMazoAEleccion(Entidad jugador) {
		habilidadesActivas.add(HabilidadActiva.robarDelMazoCartaAEleccion(jugador,1, "disminuye a los enemigos 30 pero te permite robar 2 cartas a eleccion"));
		rebarajearMesa();
		cantidadDeCartasASacar=2;
		indiceCartasASacar=0;
	}
	
	private boolean estaBloqueadoRobar(Entidad jugador) {
	    for (HabilidadActiva ha : habilidadesActivas) {
	        if (ha.getTipo() == HabilidadActiva.Tipo.BLOQUEAR_ROBAR && ha.getTurnosRestantes() > 0) {
	            if (ha.isGlobal()) return true;
	            if (ha.getObjetivo() == jugador) return true;
	        }
	    }
	    return false;
	}
	
	public void activarVerCartas() {
	    habilidadesActivas.add(HabilidadActiva.verSiguientesCartas(2, "habilidad de bill de ver cartas"));
	}

	//bloquea a todos excepto a el tirador
	public void activarBloqueoRobarATodosExcepto(Entidad caster, int turnos, String descripcion) {
	    for (Entidad e : jugadores) {
	        if (e != caster) {
	            activarBloqueoRobar(e, turnos, descripcion);
	        }
	    }
	}
	
	public void agregarCartaAJugador(Carta carta, Entidad jugador) {
		jugador.getMano().add(carta);
	}
	
	public void jugarCartaConDelay(Carta carta, Entidad jugador) {
	    if (this.cartaPendiente != null) {
	        System.out.println("Ya hay una carta en resolución. Esperá a que termine.");
	        return;
	    }
	    this.cartaPendiente = carta;
	    this.jugadorQueLaJugoPendiente = jugador;
	    this.ticksPendientes = 45;
	}

	
	private void tickHabilidadesActivasPara(Entidad jugadorQueTermino) {
	    for (int i = habilidadesActivas.size() - 1; i >= 0; i--) {
	        HabilidadActiva ha = habilidadesActivas.get(i);
	        if (ha.getObjetivo() == jugadorQueTermino) {
	            if (ha.tick()) habilidadesActivas.remove(i);
	        }
	    }
	}
	
	private void tickHabilidadesActivasMixto(Entidad jugadorQueTermino) {
	    for (int i = habilidadesActivas.size() - 1; i >= 0; i--) {
	        HabilidadActiva ha = habilidadesActivas.get(i);
	        boolean debeTickear = ha.isGlobal() || ha.getObjetivo() == jugadorQueTermino;
	        if (debeTickear && ha.tick()) habilidadesActivas.remove(i);
	    }
	}
	
	private void actualizarCartasDisponiblesMazo() {
		if(!cartasDisponiblesMazo) {
			rebarajearMesa();
		}
	}


	private void comprobarPartidaTerminada() {
		
		if(jugadores.size()==1) {
			partidaFinalizada=true;
		}
	}


	private void actualizarJugadorPerdedor() {
		 if (jugadorPerdedor != null) {
		        System.out.println("Jugador eliminado: " + jugadorPerdedor.getNombre());
		        eliminarYReacomodarJugador(jugadorPerdedor);
		        jugadorPerdedor = null;
		    }
	}
	
	private void eliminarYReacomodarJugador(Entidad jugadorAEliminar) {
	    if (jugadores.isEmpty()) return;
	    int indexEliminado = jugadores.indexOf(jugadorAEliminar);
	    if (indexEliminado == -1) return;

	    //Se agregan las cartas de la mano para no perderlas
	    mazo.addAll(jugadores.get(indexEliminado).getMano());
	    //Elimina el juagodor
	    jugadores.remove(indexEliminado);

	    if (jugadores.isEmpty()) {
	        indiceJugadorActual = 0;
	        return;
	    }

	    if (indexEliminado < indiceJugadorActual) {
	        indiceJugadorActual--;
	    } else if (indexEliminado == indiceJugadorActual) {
	    	
	        if (indiceJugadorActual >= jugadores.size()) {
	            indiceJugadorActual = 0;
	        }
	    }

	    if (indiceJugadorActual >= jugadores.size()) {
	        indiceJugadorActual = jugadores.size() - 1;
	    }
	    
	    if(jugadores.size()>1){
	    	hiloDeTiempo = new HiloTiempoPartida(this);
	        hiloDeTiempo.setMinutos(tiempo);
	        hiloDeTiempo.start();
	    }
	}


	public void jugarCarta(Carta carta, Entidad jugador) {
		Entidad enemigo = carta.getEnemigoDeterminado(jugadores,jugador);
	    obtenerRival(jugador);
	    
	    if(carta.getHabilidad() != null) {
	    	carta.getHabilidad().ejecutar(carta, jugador, enemigo, this);
	    }
	    
	    boolean tieneEstrenimiento = isHabilidadActivaEnJugador(HabilidadActiva.Tipo.ESTRENIMIENTO, jugador);
	    int puntosTiradorAntes = 0; 
	    int puntosRivalAntes = 0;  
	    
	    if (tieneEstrenimiento) {
	        puntosTiradorAntes = enemigo.getPuntos();
	        puntosRivalAntes = jugador.getPuntos();
	    }
	    
	    int chance = (int) (Math.random() * 100) + 1;
	    
	    if(chance <= 5) {
	    	System.out.println("IMHERE IMHERE IMHERE IMHERE IMHERE IMHERE IMHERE IMHERE IMHERE IMHERE IMHERE");
	    	
	    	if(!getMesa().isEmpty()) {
	    		Carta cartaTirada = getMesa().get(getMesa().size() - 1);
	    		getMesa().remove(cartaTirada);
	    		Carta imHere = new IMHERE();
	    		getMesa().add(imHere);

	    		if(imHere.getHabilidad() != null) {
	    			imHere.getHabilidad().ejecutar(imHere, jugador, enemigo, this);
	    		}

	    	}

	    }
	    
	    jugador.modificarPuntos(carta.getPuntosDisminuidos(), carta.getPorcentual());
	    enemigo.modificarPuntos(carta.getPuntosAumentadosRival(), carta.getPorcentual());
	    carta.getHabilidad().ejecutar(carta, jugador, enemigo, this);
	    
	    if (carta.getHabilidad() != null) {
	        carta.getHabilidad().ejecutar(carta, jugador, enemigo, this);
	    }
	    
	    if (tieneEstrenimiento && carta.getHabilidad() != Habilidad.INTERCAMBIO_PUNTOS) {
	        int puntosTiradorDespues = enemigo.getPuntos();
	        int puntosRivalDespues = jugador.getPuntos();
	        boolean modificoPuntos = (puntosTiradorDespues != puntosTiradorAntes) || (puntosRivalDespues != puntosRivalAntes);

	        if (!modificoPuntos) {
	        	System.out.println("EFECTO DE ESTREÑIMIENTO ACTIVADOOOOOOOOOOOOOOOOOOOOOOOO");
	            robarCartasMalas(jugador);
	            robarCartasMalas(jugador);
	            HabilidadActiva ha = buscarHabilidadActiva(HabilidadActiva.Tipo.ESTRENIMIENTO);
	            
	            if (ha != null) {
	                removeHabilidadActiva(ha);
	            }
	        } else {
	            HabilidadActiva ha = buscarHabilidadActiva(HabilidadActiva.Tipo.ESTRENIMIENTO);

	            if (ha != null) {
	                removeHabilidadActiva(ha);
	            }
	        }
	    }
	}
	
	private void repartirCartas() {
		System.out.println("Se reparten cartas");
		for (Entidad jugador : jugadores) {
	        for (int i = 0; i < 3; i++) {
	            if (!mazo.isEmpty()) {
	                Carta carta = mazo.remove(0);
	                jugador.agregarCarta(carta); 
	            }
	        }
	    }
		
	}
	
	public void robarCartaMazo(Entidad jugador) {
		robarCartaMazo(jugador, false);
		limpiarInanicionSiCorresponde(jugador);
	}
	
	public void robarCartaMazo(Entidad jugador, boolean ignorarBloqueosDeRobo) {
	    if (!ignorarBloqueosDeRobo && estaBloqueadoRobar(jugador)) {
	        System.out.println("Bloqueado: " + jugador.getNombre() + " no puede robar del mazo.");
	        return;
	    }
	    if (mazo.size() == 0) {
	        cartasDisponiblesMazo = false;
	        actualizarCartasDisponiblesMazo();
	        return;
	    }

	    // --- se roba carta del mazo ---
	    Carta carta = mazo.remove(0);

	    //pecado de la codicia
	    if (carta instanceof PecadoDeLaCodicia) {
	        for (int i = 0; i < 4 && !jugador.getMano().isEmpty(); i++) {
	            Carta removida = jugador.getMano().remove(0);
	            System.out.println("Se destruyó carta por Pecado de la Codicia: " + removida.getDescripcion());
	        }
	        jugador.agregarCarta(carta);
	        return;
	    }
	    jugador.agregarCarta(carta);
	    
	    boolean teniaMimicoPendiente = false;
	    for (HabilidadActiva ha : new ArrayList<>(habilidadesActivas)) {
	        if (ha.getTipo() == HabilidadActiva.Tipo.MIMICO_PENDIENTE && ha.getObjetivo() == jugador) {
	            teniaMimicoPendiente = true;
	            break;
	        }
	    }

	    if (teniaMimicoPendiente) {
	        if (carta.getHabilidad() == cartas.Habilidad.MIMICO) {
	            this.modificarPuntos(jugador, -30, false);
	            this.modificarPuntosGlobal(jugador, +50, false);
	            System.out.println("Mímico: robaste OTRO Mímico Vos -30 | Todos los demás +50");
	        } else {
	        	this.modificarPuntos(jugador, +70, false);
	            this.modificarPuntosGlobal(jugador, -30, false);
	            System.out.println("Mímico: robaste carta NO Mímico  Vos +70 | Todos los demás -30");
	        }

	        habilidadesActivas.removeIf(ha ->
	            ha.getTipo() == HabilidadActiva.Tipo.MIMICO_PENDIENTE && ha.getObjetivo() == jugador
	        );
	    }

	}


	public void rebarajearMesa(){
		mazo.addAll(mesa);
		mesa.clear();
		cartasDisponiblesMazo=true;
	}
	
	public void mezclarMazo() {
		Collections.shuffle(mazo);
		System.out.println("se mezcla mazo");
	}
	
	public ArrayList<Carta> getListaPorTipoCartas(TipoDeCarta tipo, ArrayList<Carta> ListaCartas1) {
		
		ArrayList<Carta> cartas = new ArrayList();
		
		for(int i=0; i<ListaCartas1.size(); i++ ) {
			if(ListaCartas1.get(i).getTipo()== tipo) {
				System.out.println("se agrego la carta  = " + ListaCartas1.get(i).getDescripcion());
				cartas.add(ListaCartas1.get(i));
			}
		}
		
		return cartas;
	}
	
	public void pasarCartas(ArrayList<Carta> ListaCartas1 , ArrayList<Carta> LiscaCartas2) {
		
		for(int i=0;i<ListaCartas1.size();i++) {
			LiscaCartas2.add(ListaCartas1.get(i));
		}
	}


	public void marcarReinicio() {
	    debeReiniciar = true;
	}

	public void actualizarReiniciarPartida() {
	    if (debeReiniciar) {
	        reiniciarPartida();
	        debeReiniciar = false;
	    }
	}

	public void eliminarPorListaCartas(ArrayList<Carta> ListaCartas1 , ArrayList<Carta> LiscaCartas2) {
		boolean encontrado= false;
		for(int i=0;i<ListaCartas1.size();i++) {
			int n=0;
			while(n<LiscaCartas2.size()|| encontrado) {
				if(LiscaCartas2.get(n).getTipo()==ListaCartas1.get(i).getTipo()) {
					System.out.println("carta eliminada = " + LiscaCartas2.get(n).getDescripcion());
					LiscaCartas2.remove(n);
				}
				n++;
			}
		}
	}
	
	public void reiniciarPartida() {
		for(Entidad jugador : jugadores) {
			jugador.getMano().clear();
		}
		mesa.clear();
		iniciarMazo();
		repartirCartas();
		
		System.out.println("Partida reiniciada por Company");
	}
	
	public void agregarCartaMesa(Carta  carta) {
		mesa.add(carta);
		aumentarIndiceMesa();
	}
	
	public void sumarRonda() {
		
		/* codigo para que ronda no se ejecute al instante;
		 *  long tiempoActual = System.currentTimeMillis();
	        // Si el tiempo actual menos el último cambio es menor al cooldown, no se ejecuta la ronda.
	        if (tiempoActual - ultimoCambioDeRonda < COOLDOWN_RONDA_MS) {
	            System.out.println("Esperando cooldown (" + COOLDOWN_RONDA_MS / 1000 + "s) para sumar otra ronda...");
	            return; // Sale del método sin ejecutar la lógica de la ronda
	        }
	        
	        // Si el cooldown ha expirado, se actualiza el tiempo y se ejecuta la acción.
	        ultimoCambioDeRonda = tiempoActual;
	       */ 
		
		this.rondas++;
		System.out.println("Se sumo una ronda");
		if (isHabilidadActivaEnJugador(HabilidadActiva.Tipo.INANICION, getJugadorActual())){
			
			getJugadorActual().modificarPuntos(getJugadorActual().getPuntos(), false);
		}
		siguienteJugador();
	}
	
	public void siguienteJugador() {
		 Entidad saliente = jugadores.get(indiceJugadorActual);
		 tickHabilidadesActivasMixto(saliente); //baja un tick a cada habilidad 

	    int cantidadJugadores = jugadores.size();
	    indiceJugadorActual = (indiceJugadorActual + direccionRonda + cantidadJugadores) % cantidadJugadores;
	}
	
	public void invertirOrden() {
	    direccionRonda *= -1;
	}
	
	public void aumentarIndiceMesa() {
		indiceMesa++;
	}

	
	@Override
	public void mostrarCartasSiguientes(int cantidad) {
	    ArrayList<Carta> cartasMostradas = new ArrayList<>();
	    for (int i = 0; i < cantidad && i < mazo.size(); i++) {
	        cartasMostradas.add(mazo.get(i));
	    }
	    setCartasMostradas(cartasMostradas);

	    System.out.println("Mostrando próximas " + cantidad + " cartas:");
	    for (Carta c : cartasMostradas) {
	        System.out.println("- " + c.getClass().getSimpleName());
	    }
	}

	private void asignarJugadorPerdedor() {
	    if (jugadores.isEmpty()) return;

	    Entidad perdedor = jugadores.get(0);
	    int mayorPuntaje = perdedor.getPuntos();

	    for (Entidad jugador : jugadores) {
	        if (jugador.getPuntos() > mayorPuntaje) {
	            perdedor = jugador;
	            mayorPuntaje = jugador.getPuntos();
	        }
	    }

	    jugadorPerdedor = perdedor;

	    if (hiloDeTiempo != null) {
	        hiloDeTiempo.terminar();
	    }
	}

	
	public Entidad getJugadorActual() {
	    if (jugadores.isEmpty()) return null;

	    if (indiceJugadorActual >= jugadores.size()) {
	        indiceJugadorActual = 0;
	    }

	    return jugadores.get(indiceJugadorActual);
	}

	
	private int getIndiceJugador() {
		return indiceJugadorActual;
	}

	public Entidad getJugador(int indice) {
		return jugadores.get(indice);
	}
	
	public ArrayList<Entidad> getJugadores() {
		return jugadores;
	}
	
	public ArrayList<Carta> getMesa() {
		return mesa;
	}
	
	public int getIndiceMesa() {
		return indiceMesa;
	}
	
	public int getDireccionRonda() {
		return direccionRonda;
	}

	public int getCantidadCartasMazo() {
		return cantidadCartasMazo;
	}
	
	public ArrayList<Carta> getCartasMostradas() {
		return cartasMostradas;
	}

	public ArrayList<Carta> getMazo() {
	    return mazo;
	}
	
	public void setCantidadCartasMazo(int cantidadCartasMazo) {
		this.cantidadCartasMazo = cantidadCartasMazo;
	}

	
	public void setCartasMostradas(ArrayList<Carta> cartasMostradas) {
	    this.cartasMostradas = cartasMostradas;
	}
	
	private void setIndiceJugador(int cambioDeIndice) {
		this.indiceJugadorActual = cambioDeIndice;
	}
	
	public boolean isPartidaFinalizada() {
		return partidaFinalizada;
	}
	
	@Override
	public void cambiarDireccion() {
		invertirOrden();
	}

	@Override
	public void robarCarta(Entidad jugador) {
		robarCartaMazo(jugador);
	}
	
	public float getProgresoTiempo() {
	    if (hiloDeTiempo == null) return 0f;
	    return hiloDeTiempo.getProgreso(); // ← accede al hilo real
	}
	
	@Override
    public void modificarPuntos(Entidad objetivo, int puntos, boolean esPorcentual) {
        objetivo.modificarPuntos(puntos, esPorcentual);
    }
	
	@Override
	public void modificarPuntosGlobal(Entidad jugadorEjecutador, int puntos, boolean esPorcentual) {
		for(Entidad jugador: jugadores) {
			if(jugador!=jugadorEjecutador) {
			jugador.modificarPuntos(puntos, esPorcentual);
			}
		}
    }


	@Override
    public void onProgresoActualizado(float nuevoProgreso) {
    }

    @Override
    public void onTiempoFinalizado() {
    		asignarJugadorPerdedor();
    }


	public void activarVerPuntos() {
		habilidadesActivas.add(HabilidadActiva.verPuntos());
	}
	
	public boolean isHabilidadActiva(HabilidadActiva.Tipo tipo) {
	    for (HabilidadActiva ha : habilidadesActivas) {
	        if (ha.getTipo() == tipo) return true;
	    }
	    return false;
	}
	
	public boolean isHabilidadActivaEnJugador(HabilidadActiva.Tipo tipo,Entidad jugador) {
		for (HabilidadActiva ha : habilidadesActivas) {
	        if (ha.getTipo() == tipo && ha.getTurnosRestantes() > 0) {
	            if (ha.getObjetivo() == jugador) return true;
	        }
	    }
	    return false;
	}
	
	public HabilidadActiva buscarHabilidadActiva (HabilidadActiva.Tipo tipo) {
		for (HabilidadActiva ha : habilidadesActivas) {
	        if (ha.getTipo() == tipo && ha.getTurnosRestantes() > 0) {
	            return ha;
	        }
	    }
	    return null;
	}
	
	public void activarEstrenimiento(Entidad rival) {
	    habilidadesActivas.add(HabilidadActiva.estrenimiento(rival, 1, "Obligado a tirar cartas que modifiquen puntos; si no, roba 2 cartas malas"));
	    System.out.println("Se activó Estrenimiento para " + rival.getNombre());
}
	
public void robarCartasMalas(Entidad jugador) {
    // Buscar una carta mala en el mazo
    Carta cartaMala = null;
    for (Carta c : mazo) {
        if (c.getTipo() == TipoDeCarta.MALA) {
            cartaMala = c;
            break;
        }
    }
    if (cartaMala != null) {
        // Robar la carta mala encontrada
        mazo.remove(cartaMala);
        jugador.agregarCarta(cartaMala);
        System.out.println(jugador.getNombre() + " robó una carta mala: " + cartaMala.getClass().getSimpleName());
    } else {
        // Si no hay cartas malas, agregar nuevas al mazo y robar una
        System.out.println("No hay cartas malas en el mazo. Agregando nuevas...");
        mazo.add(new PecadoDeLaCodicia());
        mazo.add(new Sonambulo());
        Collections.shuffle(mazo);  // Mezclar para aleatoriedad
        // Robar la primera carta mala agregada (PecadoDeLaCodicia)
        Carta nuevaMala = mazo.remove(mazo.size() - 1);  // Remover la última agregada
        jugador.agregarCarta(nuevaMala);
        System.out.println(jugador.getNombre() + " robó una carta mala generada: " + nuevaMala.getClass().getSimpleName());
    }
}

	@Override
	public void intercambiarPuntos(Entidad jugador, Entidad rival) {
	    int puntosJugador = jugador.getPuntos();
	    int puntosRival   = rival.getPuntos();

	    jugador.puntos = puntosRival;
	    rival.puntos   = puntosJugador;
	}


	public void removeHabilidadActiva(HabilidadActiva habilidad) {
		habilidadesActivas.remove(habilidad);
	}


	public int getcantidadDeCartasASacar() {
		return this.cantidadDeCartasASacar;
	}
	
	public int getIndiceDeCartasASacar() {
		return this.indiceCartasASacar;
	}
	public void modificarIndiceDeCartasASacar(int numeroCambiado) {
		
		this.indiceCartasASacar=numeroCambiado;;
	}


	public void restarcantidadDeCartasASacar() {
		this.cantidadDeCartasASacar--;
	}

	private Entidad obtenerRival(Entidad jugador) {
	    for (Entidad e : jugadores) {
	        if (!e.equals(jugador)) {
	            return e;
	        }
	    }
	    return null;
	}


	public void activarMimicoPendiente(Entidad jugador) {
		for (HabilidadActiva ha : habilidadesActivas) {
	        if (ha.getTipo() == HabilidadActiva.Tipo.MIMICO_PENDIENTE && ha.getObjetivo() == jugador) {
	            return;
	        }
	    }
	    habilidadesActivas.add(HabilidadActiva.mimicoPendiente(jugador));
	}


	public void activarBloqueoActivo(Entidad jugador) {
	    habilidadesActivas.add(HabilidadActiva.bloqueoActivo(jugador));
	    System.out.println("Bloqueo ACTIVADO por " + jugador.getNombre());
	}
	
	public boolean hayCartaPendiente() {
	    return cartaPendiente != null;
	}

}