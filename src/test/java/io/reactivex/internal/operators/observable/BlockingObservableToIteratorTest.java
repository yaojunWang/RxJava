/**
 * Copyright 2016 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */

package io.reactivex.internal.operators.observable;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;

import org.junit.*;

import io.reactivex.*;
import io.reactivex.disposables.Disposables;
import io.reactivex.exceptions.TestException;

public class BlockingObservableToIteratorTest {

    @Test
    public void testToIterator() {
        Observable<String> obs = Observable.just("one", "two", "three");

        Iterator<String> it = obs.blockingIterable().iterator();

        assertEquals(true, it.hasNext());
        assertEquals("one", it.next());

        assertEquals(true, it.hasNext());
        assertEquals("two", it.next());

        assertEquals(true, it.hasNext());
        assertEquals("three", it.next());

        assertEquals(false, it.hasNext());

    }

    @Test(expected = TestException.class)
    public void testToIteratorWithException() {
        Observable<String> obs = Observable.unsafeCreate(new ObservableSource<String>() {

            @Override
            public void subscribe(Observer<? super String> observer) {
                observer.onSubscribe(Disposables.empty());
                observer.onNext("one");
                observer.onError(new TestException());
            }
        });

        Iterator<String> it = obs.blockingIterable().iterator();

        assertEquals(true, it.hasNext());
        assertEquals("one", it.next());

        assertEquals(true, it.hasNext());
        it.next();
    }

    @Ignore("subscribe() should not throw")
    @Test(expected = TestException.class)
    public void testExceptionThrownFromOnSubscribe() {
        Iterable<String> strings = Observable.unsafeCreate(new ObservableSource<String>() {
            @Override
            public void subscribe(Observer<? super String> observer) {
                throw new TestException("intentional");
            }
        }).blockingIterable();
        for (String string : strings) {
            // never reaches here
            System.out.println(string);
        }
    }
}
