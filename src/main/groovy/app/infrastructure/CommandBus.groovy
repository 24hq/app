package app.infrastructure

interface CommandBus {

    public <R, C extends Command<R>> R execute(C command)

}
