package app.infrastructure

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import static com.google.common.base.Preconditions.checkNotNull
import static org.springframework.core.GenericTypeResolver.resolveTypeArguments

@Component
class DefaultCommandBus implements CommandBus {

    Map<Class<?>, CommandHandler> commandTypeToHandler = [:]

    @Autowired
    public DefaultCommandBus(Collection<CommandHandler> handlers) {
        for (CommandHandler handler : handlers) {
            commandTypeToHandler << [(commandOf(handler)): handler]
        }
    }

    private commandOf(handler) {
        return resolveTypeArguments(handler.class, CommandHandler)[1]
    }

    @Override
    public <R, C extends Command<R>> R execute(C command) {
        checkNotNull(command, "Command cannot be null", '')
        CommandHandler<R, C> handler = getHandlerFor(command)
        handler.handle(command)
    }

    private getHandlerFor(command) {
        checkNotNull(commandTypeToHandler[(command.class)], "Handler cannot be null ($command)", '')
    }

}
