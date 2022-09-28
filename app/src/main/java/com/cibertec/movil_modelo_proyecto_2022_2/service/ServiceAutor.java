package com.cibertec.movil_modelo_proyecto_2022_2.service;




import com.cibertec.movil_modelo_proyecto_2022_2.entity.Autor;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ServiceAutor{
    @POST("autor")

    public Call<Autor> insertaAutor(@Body Autor obj);
}
