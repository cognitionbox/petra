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
package io.cognitionbox.petra.lang.impls.collections;

import io.cognitionbox.petra.lang.impls.BaseExecutionModesTest;
import io.cognitionbox.petra.config.ExecMode;

import io.cognitionbox.petra.lang.Ref;
import io.cognitionbox.petra.util.impl.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.Serializable;
import java.util.LinkedList;

import static io.cognitionbox.petra.util.Petra.ref;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class PCollectionsTest extends BaseExecutionModesTest {
  public PCollectionsTest(ExecMode execMode) {
    super(execMode);
  }

  /*
   * Focuses on testing rw info is kept when serializing/de-serializing on hazelcast
   */

  static class IntegerLinkedList extends LinkedList<Integer> {}
  static class IntegerArrayList extends java.util.ArrayList<Integer> {}
  static class IntegerQueue extends LinkedList<Integer> {}
  static class IntegerSet extends PSet<Integer> {}
  static class IntegerMap extends PMap<Integer,Integer> {}
  static class IntegerConcurrentLinkedQueue extends java.util.concurrent.ConcurrentLinkedQueue {}
  static class IntegerPArrayBlockingQueue extends java.util.concurrent.ArrayBlockingQueue {
    public IntegerPArrayBlockingQueue(int elements) {
      super(elements);
    }
  }
  static class IntegerPLinkedBlockingQueue extends java.util.concurrent.LinkedBlockingQueue {}
  static class IntegerPLinkedTransferQueue extends java.util.concurrent.LinkedTransferQueue {}

  @Test
  public void testLinkedList(){
    Ref ref = ref(new IntegerLinkedList());
    assertThat(ref.get()).isInstanceOf(IntegerLinkedList.class);
  }
  @Test
  public void testArrayList(){
    Ref ref = ref(new IntegerArrayList());
    assertThat(ref.get()).isInstanceOf(IntegerArrayList.class);
  }


  @Test
  public void testArrayBlockingQueue(){
    Ref ref = ref(new IntegerPArrayBlockingQueue(10));
    assertThat(ref.get()).isInstanceOf(IntegerPArrayBlockingQueue.class);
  }

  @Test
  public void testIntegerXLinkedBlockingQueue(){
    Ref ref = ref(new IntegerPLinkedBlockingQueue());
    assertThat(ref.get()).isInstanceOf(IntegerPLinkedBlockingQueue.class);
  }

  @Test
  public void testIntegerXLinkedTransferQueue(){
    Ref ref = ref(new IntegerPLinkedTransferQueue());
    assertThat(ref.get()).isInstanceOf(IntegerPLinkedTransferQueue.class);
  }

  @Test
  public void testSet(){
    Ref<IntegerSet> ref = ref(new IntegerSet());

    assertThat(ref.get()).isInstanceOf(IntegerSet.class);
  }

  @Test
  public void testMap(){
    Ref ref = ref(new IntegerMap());
    assertThat(ref.get()).isInstanceOf(IntegerMap.class);
  }

  public static class IntegerSetBox implements Serializable {
    private IntegerSet set;

    public IntegerSet getSet() {
      return set;
    }

    public IntegerSetBox(IntegerSet set) {
      this.set = set;
    }
  }

  @Test
  public void testBoxedSet(){
    // when boxed by another class, does not rw info in all modes
    Ref<IntegerSetBox> ref = ref(new IntegerSetBox(new IntegerSet()));
    assertThat(ref.get().getSet()).isInstanceOf(IntegerSet.class);
  }
}