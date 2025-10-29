package juegos;

import Entidades.Entidad;

public class HabilidadActiva {
    public enum Tipo {
        BLOQUEAR_ROBAR,
        MOSTRAR_PUNTOS,
        SONAMBULO,
        MOSTRAR_CARTAS_SIGUIENTES,
        ROBAR_CARTAS_A_ELECCION,
    	INANICION,
    	COLERA,
    	ESTRENIMIENTO,
    	MIMICO_PENDIENTE, BLOQUEO_ACTIVO,;
    }

    private final Tipo tipo;
    private final Entidad objetivo;   
    private final boolean global;      
    private int turnosRestantes;
    private final String descripcion;

    public HabilidadActiva(Tipo tipo, Entidad objetivo, int turnos, String descripcion, boolean global) {
        this.tipo = tipo;
        this.objetivo = objetivo;
        this.turnosRestantes = Math.max(1, turnos);
        this.descripcion = descripcion;
        this.global = global;
    }

    
    public Tipo getTipo() { return tipo; }
    public Entidad getObjetivo() { return objetivo; }
    public boolean isGlobal() { return global; }
    public int getTurnosRestantes() { return turnosRestantes; }
    public String getDescripcion() { return descripcion; }

    public boolean tick() { return --turnosRestantes <= 0; }

    // Fábricas
    public static HabilidadActiva bloqueoRobarGlobal(int turnos, String desc) {
        return new HabilidadActiva(Tipo.BLOQUEAR_ROBAR, null, turnos, desc, true);
    }
    public static HabilidadActiva bloqueoRobarA(Entidad objetivo, int turnos, String desc) {
        return new HabilidadActiva(Tipo.BLOQUEAR_ROBAR, objetivo, turnos, desc, false);
    }

	public static HabilidadActiva verPuntos() {
		return new HabilidadActiva(Tipo.MOSTRAR_PUNTOS, null, 2, "Ver puntos", true);
	}

	public static HabilidadActiva jugarCartaAleatorea(Entidad objetivo, int turnos, String desc) {
		return new HabilidadActiva(Tipo.SONAMBULO, objetivo, turnos, desc, false);
	}
	
	public static HabilidadActiva verSiguientesCartas(int turnos, String desc) {
	    return new HabilidadActiva(Tipo.MOSTRAR_CARTAS_SIGUIENTES, null, turnos, desc, true);
	}

	public static HabilidadActiva verCartas() {
		System.out.println("Se activo el bill");
	    return new HabilidadActiva(Tipo.MOSTRAR_CARTAS_SIGUIENTES, null, 1, "Ver próximas cartas", true);
	}

	public static HabilidadActiva robarDelMazoCartaAEleccion(Entidad jugador, int turnos, String descripcion2) {
		System.out.println("Se activo el hambre");
		return new HabilidadActiva(Tipo.ROBAR_CARTAS_A_ELECCION, jugador, 2, "Podes robar la carta a eleccion del mazo", true);
	}
	
	public static HabilidadActiva colera(Entidad jugador, int turnos, String descripcion2) {
		System.out.println("Se activo el hambre");
		return new HabilidadActiva(Tipo.COLERA, jugador, turnos, "Te obliga a robar 3 cartas y si ninguna carta es mala por 3 turnos disminuye 20", false);
	}


	public static HabilidadActiva inanicion(Entidad jugador, int i, String string) {
		return new HabilidadActiva(Tipo.INANICION, jugador, i, string, false);
	}
	
	public static HabilidadActiva estrenimiento(Entidad objetivo, int turnos, String desc) {
	    return new HabilidadActiva(Tipo.ESTRENIMIENTO, objetivo, turnos, desc, false);
	}
	
	public static HabilidadActiva mimicoPendiente(Entidad jugador) {
		return new HabilidadActiva(Tipo.MIMICO_PENDIENTE, jugador, 9999,
	            "Mímico pendiente: al próximo robo se evalúa el efecto", false);
	}
	
	public static HabilidadActiva bloqueoActivo(Entidad jugador) {
	    return new HabilidadActiva(Tipo.BLOQUEO_ACTIVO, jugador, 2,
	        "Bloquea la próxima carta en resolución.", false);
	}
	
}
