package dds.monedero.model;

import dds.monedero.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MonederoTest {
  private Cuenta cuenta;

  @BeforeEach
  void init() {
    cuenta = new Cuenta();
  }

  @Test
  void Poner1500() {
    cuenta.poner(1500);
    assertEquals(cuenta.getSaldo(),1500);
  }

  @Test
  void HagoTresDepositos() {
    cuenta.poner(1500);
    cuenta.poner(456);
    cuenta.poner(1900);

    assertEquals(cuenta.getMovimientos().size(),3);
  }

  @Test
  void HagoMasDeTresDepositos() {
    assertThrows(cuentaExcepcion.class, () -> {
          cuenta.poner(1500);
          cuenta.poner(456);
          cuenta.poner(1900);
          cuenta.poner(245);
    });
  }

  @Test
  void ExtraerMasQueElSaldo() {
    assertThrows(cuentaExcepcion.class, () -> {
          cuenta.setSaldo(90);
          cuenta.sacar(1001);
    });
  }

  @Test
  public void ExtraerMasDe1000() {
    assertThrows(cuentaExcepcion.class, () -> {
      cuenta.setSaldo(5000);
      cuenta.sacar(1001);
    });
  }

  @Test
  public void ExtraerMontoNegativo() {
    assertThrows(cuentaExcepcion.class, () -> cuenta.sacar(-500));
  }

}