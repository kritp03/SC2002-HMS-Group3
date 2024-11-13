package HMS.src.medication;

public enum ReplenishmentRequestStatus {
  PENDING,
  APPROVED,
  REJECTED,
  FULFILLED;

  public String showStatusByColor(){
      switch (this){
          case PENDING:
              return "\u001B[33m" + this + "\u001B[0m";
          case APPROVED:
              return "\u001B[32m" + this + "\u001B[0m";
          case REJECTED:
              return "\u001B[31m" + this + "\u001B[0m";
          case FULFILLED:
              return "\u001B[36m" + this + "\u001B[0m";
          default:
              return this.toString();
      }
  }
}
