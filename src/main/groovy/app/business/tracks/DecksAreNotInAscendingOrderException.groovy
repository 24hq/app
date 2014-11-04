package app.business.tracks

import app.infrastructure.throwables.ApplicationException
import groovy.transform.Immutable

@Immutable
class DecksAreNotInAscendingOrderException extends ApplicationException {

    String trackCode

}
