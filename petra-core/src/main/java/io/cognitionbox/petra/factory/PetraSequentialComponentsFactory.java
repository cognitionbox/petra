/**
 * Copyright 2016-2020 Aran Hakki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.cognitionbox.petra.factory;

import com.google.common.util.concurrent.MoreExecutors;
import io.cognitionbox.petra.core.IRingbuffer;
import io.cognitionbox.petra.core.engine.petri.Place;
import io.cognitionbox.petra.core.engine.petri.impl.ConcurrentHashMapPlace;
import io.cognitionbox.petra.core.engine.petri.impl.HashMapPlace;
import io.cognitionbox.petra.core.impl.AbstractRef;
import io.cognitionbox.petra.core.impl.MapBasedRingBuffer;
import io.cognitionbox.petra.lang.RGraphComputer;
import io.cognitionbox.petra.lang.Ref;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class PetraSequentialComponentsFactory implements IPetraComponentsFactory {

  public static ExecutorService blockingExecutorService(int queueSize, int threadCount) {
    BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(queueSize);
    ThreadPoolExecutor tpe = new ThreadPoolExecutor(threadCount, threadCount, 20, TimeUnit.SECONDS, queue);
    return tpe;
  }

  @Override
  public IRingbuffer createRingbuffer(String name) {
    return new MapBasedRingBuffer(name,Long.MAX_VALUE);
  }

  @Override
  public ExecutorService createExecutorService(String name){
    throw new UnsupportedOperationException();
    //return MoreExecutors.newDirectExecutorService();
    //return blockingExecutorService(1,Runtime.getRuntime().availableProcessors()*2);
//      return new ExecutorService(){
//
//        @Override
//        public void execute( Runnable command) {
//          command.run();
//        }
//
//        @Override
//        public void shutdown() {
//
//        }
//
//
//        @Override
//        public List<Runnable> shutdownNow() {
//          throw new UnsupportedOperationException();
//        }
//
//        @Override
//        public boolean isShutdown() {
//          return false;
//        }
//
//        @Override
//        public boolean isTerminated() {
//          return false;
//        }
//
//        @Override
//        public boolean awaitTermination(long timeout,  TimeUnit unit) throws InterruptedException {
//          return false;
//        }
//
//
//        @Override
//        public <T> Future<T> submit( Callable<T> task) {
//          try {
//            CompletableFuture<T> completableFuture = new CompletableFuture<>();
//            completableFuture.complete(task.call());
//            return completableFuture;
//          } catch (Exception e) {
//            e.printStackTrace();
//          }
//          return null;
//        }
//
//
//        @Override
//        public <T> Future<T> submit( Runnable task, T result) {
//          try {
//            task.run();
//            CompletableFuture<T> completableFuture = new CompletableFuture<>();
//            completableFuture.complete(result);
//            return completableFuture;
//          } catch (Exception e) {
//            e.printStackTrace();
//          }
//          return null;
//        }
//
//
//        @Override
//        public Future<?> submit( Runnable task) {
//          try {
//            task.run();
//            CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();
//            completableFuture.complete(true);
//            return completableFuture;
//          } catch (Exception e) {
//            e.printStackTrace();
//          }
//          return null;
//        }
//
//
//        @Override
//        public <T> List<Future<T>> invokeAll( Collection<? extends Callable<T>> tasks) throws InterruptedException {
//          return tasks.stream().map(t->submit(t)).collect(Collectors.toList());
//        }
//
//
//        @Override
//        public <T> List<Future<T>> invokeAll( Collection<? extends Callable<T>> tasks, long timeout,  TimeUnit unit) throws InterruptedException {
//          return tasks.stream().map(t->submit(t)).collect(Collectors.toList());
//        }
//
//
//        @Override
//        public <T> T invokeAny( Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
//          throw new UnsupportedOperationException();
//        }
//
//        @Override
//        public <T> T invokeAny( Collection<? extends Callable<T>> tasks, long timeout,  TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
//          throw new UnsupportedOperationException();
//        }
//      };
  }

  @Override
  public Lock createLock(String name) {
    return new ReentrantLock();
  }

  @Override
  public Set createSet(String name) {
    return new HashSet<>();
  }

  @Override
  public Queue createQueue(String name) {
    return new LinkedList();
  }

  @Override
  public List createList(String name) {
    return new ArrayList();
  }

  @Override
  public <T> Stream<T> createStreamFromList(List<T> list) {
    return list.stream();
  }


  @Override
  public <K,V> Stream<Entry<K,V>> createStreamFromMap(Map<K,V> map) {
    return map.entrySet().stream();
  }

  @Override
  public <T,V> Stream<T> createStreamFromSet(Set<T> set) {
    return set.stream();
  }

  @Override
  public <T> Stream<T> createStreamFromIterable(Iterable<T> iterable) {
    return StreamSupport.stream(iterable.spliterator(), false);
  }

  @Override
  public Map createMap(String s) {
    return new HashMap<>();
  }

  @Override
  public Place createPlace(String name) {
    return new HashMapPlace(name);
  }

  private static class AtomicReferenceImpl<T> extends AbstractRef<T> {

    private AtomicReference<T> atomicReference;

    @Override
    public String toString() {
      return "AtomicReferenceImpl{" +
          "ref=" + atomicReference +
          '}';
    }

    public AtomicReferenceImpl(T value){
      atomicReference = new AtomicReference<>(value);
    }

    @Override
    public T get() {
      return atomicReference.get();
    }

    @Override
    public void set(T value) {
      atomicReference.set(value);
    }
  }

  @Override
  public <T> Ref<T> createRef(T value, String name) {
    return new AtomicReferenceImpl(value);
  }
}
