package gianni_bussoletti.beu2s3g1.payloads;

import java.time.LocalDateTime;

//Un record permette di non dover scrivere Costruttori e Getter, perché il record li tiene già in conto e li crea dietro le quinte
public record ErrorsDTO(String message, LocalDateTime timestamp) {
}
