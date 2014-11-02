package app.infrastructure

interface CommandHandler<R, C extends Command<R>> {

    rx.Observable<R> handle(C command)

}