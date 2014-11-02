package app.infrastructure.rx

import org.springframework.web.context.request.async.DeferredResult

class Deferrables {

    public static <R> DeferredResult<R> deferred(rx.Observable<R> observable) {
        def result = new DeferredResult<>(1500)
        def observer = new rx.Observer() {

            @Override
            void onCompleted() {

            }

            @Override
            void onError(Throwable e) {
                result.errorResult = e
            }

            @Override
            void onNext(Object r) {
                result.result = r
            }
        }

        observable.subscribe(observer)
        result
    }

}
