package com.GhostlyRunes;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

/**
 * Created by Miguel on 23/01/2017.
 */

public class SoundHandler {

    private Context myContext;
    private SoundPool sound;
    private float rate = 1.0f;
    private float leftVolume = 0.5f;
    private float rightVolume = 0.5f;

    //La clase SoundPool administra y ejecuta todos los recursos de audio de la aplicacion.
    //Nuestro constructor, que determina la configuracion de audio del contexto de nuestra aplicacion

    @SuppressWarnings("deprecation")
    public SoundHandler(Context ctx)
    {
        this.myContext = ctx;
        this.sound = new SoundPool(16, AudioManager.STREAM_MUSIC, 100);



    }

    //Obtiene el sonido y retorna el id del mismo
    public int load(int idSonido)
    {
        return sound.load(myContext, idSonido, 1);
    }

    //Ejecuta el sonido, toma como parametro el id del sonido a ejecutar.
    public void play(int idSonido)
    {
        sound.play(idSonido, leftVolume, rightVolume, 1, 0, rate);
    }

    // Libera memoria de todos los objetos del sndPool que ya no son requeridos.
    public void unloadAll()
    {
        sound.release();
    }
}
