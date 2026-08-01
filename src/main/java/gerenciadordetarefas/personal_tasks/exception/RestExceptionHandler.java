package gerenciadordetarefas.personal_tasks.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroRespostaDTO> tratarErroJsonInvalido(HttpMessageNotReadableException exception){

       ErroRespostaDTO erro =  new ErroRespostaDTO(
               LocalDateTime.now(),
               400,
               "BAD_REQUEST",
               "um ou mais campos estão invalidos ou fora do padrão esperado."
       );
       return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ErroRespostaDTO> tratarRegraNegocio(RegraNegocioException exception){
        ErroRespostaDTO erro = new ErroRespostaDTO(
                LocalDateTime.now(),
                400,
                "BAD_REQUEST",
                exception.getMessage()
        );
        return ResponseEntity.status( HttpStatus.BAD_REQUEST).body(erro);
    }
}
