package Utiles;

public final class Headless {
    private static boolean on = false;
    public static void activar()  { on = true;  }
    public static void desactivar(){ on = false; }
    public static boolean activo(){ return on; }
}

