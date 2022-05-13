package dds.monedero.model;

import dds.monedero.exceptions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {

  private double saldo = 0;
  private List<Movimiento> movimientos = new ArrayList<>();

  public Cuenta() {
    saldo = 0;
  }

  public Cuenta(double montoInicial) {
    saldo = montoInicial;
  }

  public void setMovimientos(List<Movimiento> movimientos) {
    this.movimientos = movimientos;
  }

  public void poner(double cuanto) {
    revisarMonto(cuanto);

    if (getMovimientos().stream().filter(movimiento -> movimiento.isDeposito()).count() >= 3) {
      throw new cuentaExcepcion("Ya excedio los " + 3 + " depositos diarios");
    }

    agregarMovimiento(new Movimiento(LocalDate.now(), cuanto, true));
  }

  public void sacar(double cuanto) {
    revisarMonto(cuanto);

    if (getSaldo() - cuanto < 0) {
      throw new cuentaExcepcion("No puede sacar mas de " + getSaldo() + " $");
    }
    double montoExtraidoHoy = getMontoExtraidoA(LocalDate.now());
    double limite = 1000 - montoExtraidoHoy;
    if (cuanto > limite) {
      throw new cuentaExcepcion("No puede extraer mas de $ " + 1000
          + " diarios, límite: " + limite);
    }
    agregarMovimiento(new Movimiento(LocalDate.now(), cuanto, false));
  }

  public void agregarMovimiento(Movimiento movimiento){
    double saldo = calcularSaldoPorMovimiento(movimiento);
    setSaldo(saldo);
    movimientos.add(movimiento);
  }

  public double getMontoExtraidoA(LocalDate fecha) {
    return getMovimientos().stream()
        .filter(movimiento -> !movimiento.fueExtraido(fecha))
        .mapToDouble(Movimiento::getMonto)
        .sum();
  }

  public List<Movimiento> getMovimientos() {
    return movimientos;
  }

  public double getSaldo() {
    return saldo;
  }

  public void setSaldo(double saldo) {
    this.saldo = saldo;
  }

  public void revisarMonto(double cuanto){
    if (cuanto <= 0) {
      throw new cuentaExcepcion(cuanto + ": el monto a ingresar debe ser un valor positivo");
    }
  }

  public double calcularSaldoPorMovimiento(Movimiento movimiento){
    if (movimiento.isDeposito()) {
      return getSaldo() + movimiento.getMonto();
    } else {
      return getSaldo() - movimiento.getMonto();
    }
  }
}
