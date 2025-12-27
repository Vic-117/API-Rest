/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vPerez.ProgramacionNCapasNov2025.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 *
 * @author digis
 */
@Configuration
public class GlobalCorsConfiguration {
    
    @Bean
    public CorsFilter corsFilter(){
        //Para configurar las URL aceptadas o no
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        //Configurar los permisos
        CorsConfiguration corsConfig = new CorsConfiguration();
        
        corsConfig.addAllowedOrigin("*");
        corsConfig.addAllowedMethod("GET");
        corsConfig.addAllowedMethod("POST");
        corsConfig.addAllowedMethod("PUT");
        corsConfig.addAllowedMethod("DELETE");
        corsConfig.addAllowedMethod("PATCH");
        
        corsConfig.addAllowedHeader("*");
        source.registerCorsConfiguration("/**", corsConfig);
        return new CorsFilter(source);
    }
    
}
