package app.infrastructure

interface CommandBus {

    public <R, C extends Command<R>> rx.Observable<R> execute(C command)

}
