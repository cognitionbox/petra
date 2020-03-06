# petra #

[![Apache 2.0](https://img.shields.io/badge/License-Apache%202.0-brightgreen?style=shield)](https://www.apache.org/licenses/LICENSE-2.0) [![<CognitionBox>](https://circleci.com/gh/cognitionbox/petra.svg?style=shield)](https://circleci.com/gh/cognitionbox) [![contributions welcome](https://img.shields.io/badge/contributions-welcome-brightgreen.svg?style=shield)](https://github.com/cognitionbox/petra)

## Overview ##

### What is Petra? ###

In simple terms Petra is a concise language that extends Java 8.
It achieves this through a general purpose DSL coded in Java 8.

Petra = Automatic Parallelization + 
            State programming made easy + 
                Visualization of Flows +
                    Software Verification             
                  
Below is an example visualization of a Petra defined program.
This visualization can only be generated if Petra's type system verifies that the program passes a
strict set of safety properties. These visualizations will always be in sync with the program as 
they are generated directly from the developers code.   
                
![Alt text](https://g.gravizo.com/svg?digraph%20Petra%20{rankdir=%22LR%22;start%20[shape=circle,%20style=filled,%20fillcolor=green];start-%3EX;stop%20[shape=doubleoctagon,%20style=filled,%20fillcolor=red];Y-%3Estop;X%20[style=filled,%20fillcolor=blue%20fontcolor=white];Y%20[style=filled,%20fillcolor=blue%20fontcolor=white];XtoY%20[shape=rect%20style=filled,%20fillcolor=orange];PreA%20[style=filled,%20fillcolor=blue%20fontcolor=white];PostA%20[style=filled,%20fillcolor=blue%20fontcolor=white];AtoA%20[shape=rect%20style=filled,%20fillcolor=orange];PreB%20[style=filled,%20fillcolor=blue%20fontcolor=white];PostB%20[style=filled,%20fillcolor=blue%20fontcolor=white];BtoB%20[shape=rect%20style=filled,%20fillcolor=orange];X-%3EXtoY%20[label=PRE];XtoY-%3EY%20[label=POST];X-%3EB%20[label=extract];B%20[style=filled,%20fillcolor=blue%20fontcolor=white];X-%3EA%20[label=extract];A%20[style=filled,%20fillcolor=blue%20fontcolor=white];PreA-%3EAtoA%20[label=PRE];AtoA-%3EPostA%20[label=POST];PreB-%3EBtoB%20[label=PRE];BtoB-%3EPostB%20[label=POST];PostA-%3EXtoY_join_0%20[label=PRE];XtoY_join_0-%3EY%20[label=POST];XtoY_join_0%20[shape=rect%20style=filled,%20fillcolor=orange%20fontcolor=black];PostB-%3EXtoY_join_0%20[label=PRE];A-%3EPostA%20[label=assignable];A-%3EPreA%20[label=assignable];B-%3EPostB%20[label=assignable];B-%3EPreB%20[label=assignable];XtoY-%3EAtoA%20[style=dotted,%20label=owns];XtoY-%3EBtoB%20[style=dotted,%20label=owns];XtoY_join_0%20[shape=rect%20style=filled,%20fillcolor=orange%20fontcolor=black];XtoY-%3EXtoY_join_0%20[style=dotted,%20label=owns];})
                    
                    
The aim of Petra is to provide a modern programming paradigm, 
a concise language and tooling to meet today's complex software needs. 
Petra has verification in mind at it has been designed from the ground up to 
produce safe, regularized, concise, object-orientated, parallel, distributed systems, 
which are easy to reason about and to minimize and detected 
programming errors, whilst being a practical tool for industry.
 
Petra's reference implementation is a general purpose embedded Java 8 DSL 
(Domain Specific Language), with built in automatic software verification features.
The embedded DSL has been designed in a way that makes it feel like a native language.
Most embedded DSLs in Java use method chaining, however Petra uses a function sequence pattern
for better readability. Future versions will support method chaining in addition to the 
function sequence pattern as it is useful for dynamically / programmatically building up statements. 
This would be useful in an AI system that needs to write and verify it's own code, 
however for initial use cases the function sequence pattern will be more than enough.

Petra aims to be purely functional and Petra programs are constructed in a declarative manor.
Petra has parallel and distributed programming built into its core and with little work
a program can be executed in sequential, parallel or distributed modes, 
whilst maintaining consistent semantics.

Petra's type system resembles a type of abstract rewriting system, more specifically a Ground rewriting system, 
which is a term rewriting system that has only non-variable terms. 
Ground rewriting systems have decidable termination i.e. it is always possible to get a yes/no answer for
whether or not a ground rewriting system terminates. This is very useful for writing provable program specifications 
which useful properties including termination, reachability and the presence of cycles can be tested for.

Petra's rewriting system has yet to be defined formally, however the evaluation of the rewrite rules 
are captured in the Java reference implementation of Petra in this repo within the ```GraphOutputCannotBeReachedFromInput``` 
class (located under the ```io.cognitionbox.petra.guarantees.impls``` package) 
which tests a Petra program for reachability.

In order to get a feel for abstract rewrite systems take a look at Lindenmayer systems 
([L-systems](https://en.wikipedia.org/wiki/L-system)), which is a simple type of string rewriting system. L-systems were introduced and developed in 1968 by 
Aristid Lindenmayer, a Hungarian theoretical biologist and botanist at the University of Utrecht. 
Lindenmayer used L-systems to describe the behaviour of plant cells and to model the growth processes 
of plant development.

Petra's execution model resembles a unique extension to Workflow-nets (a type of Petri-net that have desirable safety properties).
More specifically Petra can be described as a ```Read/Write/Consume, Recursively Extractable Object-orientated, Dynamic, Coloured, Workflow-net with Regular Iteration```.

Petri-nets were invented by Carl Adam Petri in 1939, for the purpose of describing chemical processes.
A basic description of Petri-net and Workflow-net mechanics is provided below.

### Petri-nets ###
http://mlwiki.org/index.php/Petri_Nets

### Workflow-nets ###
http://mlwiki.org/index.php/Workflow_Nets

### What is this repository for? ###

This repo contains the following projects: 

#### petra-core ####
Java source code for the reference implementation of Petra.

#### petra-examples ####
Simple examples for using Petra with Java.

#### petra-hazelcast ####
Java implementation for the Hazelcast IMDG implementation of the Petra components
factory ```IPetraComponentsFactory```. This enables Petra to be executed using
distributed objects using Hazelcast's in-memory data grid technology.
 
#### petra-hazelcast-jet ####
Java implementation for the Hazelcast JET implementation of the Petra components
factory ```IPetraComponentsFactory```. This enables Petra to be executed using
distributed objects using Hazelcast's in-memory data grid technology and 
Jet streaming technology. Jet can process large distributed data structures with
lower latency.

#### petra-kotlin ####
A tiny Kotlin library to make it even easier to use Kotlin with Petra.

#### petra-tests ####
Tests for all execution modes of Petra. ```SEQ```, ```PAR``` and ```DIS``` modes
(Sequential, Parallel and Distributed modes).

### Distributed Deployment Architecture ###

Petra can be deployed against any Hazelcast cluster hosted on-site or on in the cloud or directly on Hazelcast Cloud for convenience.
Petra worker nodes can run on any Java 8 environment. When Petra worker nodes start they compete to become the master node.
There can only be one master node, all other nodes will be workers. 
The master node owns the root level Petra iteration loop. If the master goes down one of the other worker nodes
will try to become the master in order to own and execute the root iteration loop.
Below is a simple diagram showing the distributed deployment architecture. 

![Alt text](https://g.gravizo.com/svg?digraph%20PetraArchitecture%20{rankdir=LR;%22Petra%20worker%20node%201%20(Master)%22-%3E%22Hazelcast%20IMDG%20/%20JET%20cluster%22%22Petra%20worker%20node%202%22-%3E%22Hazelcast%20IMDG%20/%20JET%20cluster%22%22Petra%20worker%20node%203%22-%3E%22Hazelcast%20IMDG%20/%20JET%20cluster%22%22Petra%20worker%20node%20n%20...%22-%3E%22Hazelcast%20IMDG%20/%20JET%20cluster%22})
     

### How do I get set up? ###

Simply clone the repo and navigate to the project's root directory then do
```mvn clean install``` to build and install the project jars to your local maven repo.

This will build the projects in the order as defined by parent pom file (see below)

```
<modules>
    <module>petra-core</module>
    <module>petra-examples</module>
    <module>petra-kotlin</module>
    <module>petra-hazelcast</module>
    <module>petra-hazelcast-jet</module>
    <module>petra-tests</module>
</modules>
```

Then you can include the ```petra-core```, ```petra-hazelcast``` and/or
 ```petra-kotlin``` dependencies in your projects (see below).
```petra-hazelcast-jet``` needs some more development before its ready.

```
<dependency>
	<groupId>io.cognitionbox.petra</groupId>
	<artifactId>petra-core</artifactId>
	<version>pre-alpha</version>
</dependency>
```

```
<dependency>
	<groupId>io.cognitionbox.petra</groupId>
	<artifactId>petra-hazelcast</artifactId>
	<version>pre-alpha</version>
</dependency>
```

```
<dependency>
	<groupId>io.cognitionbox.petra</groupId>
	<artifactId>petra-kotlin</artifactId>
	<version>pre-alpha</version>
</dependency>
```

Please look at the tests within the examples package to start understanding Petra.
```petra-core``` includes support for sequential and parallel execution modes, 
however Petra has been constructed in a way that will allow for distributed mode implementations.

### Petra's Programming Paradigm ###

Petra is a hybrid of Object-orientated, Pure Functional and 
Parallel / Distributed Programming.
Petra aim's to make it impossible to do anything that is "impure".

Petra's three main programming components, PGraph, PEdge and PJoin (see below)
are analogous to pure functions, even when they mutating state i.e. causing a side-effect.
This means a complex system can be composed of a hierarchy of function calls, 
whilst being able to mutate state in a safe and pure way.

Petra aims to parallelize at all opportunities whilst removing the
developers overhead to hand code parallel algorithms using traditional
concurrency primitives like threads, locks and fork/joins, etc.
Multiple PGraph and PEdge steps run in parallel where possible given their data dependencies. 
The internals of PJoin makes use of parallel execution when filtering objects from a 
PGraph's place during the matching process (when using parallel execution mode).   

Petra features a form of transactional memory which allows it to rollback to "good" states,
before retrying computations, this helps to provide a pure programming environment, 
where its clear to the programmer and the runtime what state the system is actually in.

### Use Cases ###
Petra is well suited to back-end processing, including but not limited to,
critical business processes, infrastructure orchestration, scientific workflows, 
modelling and execution of business processes, AI, machine learning, 
smart contract execution and blockchain applications.

### Petra Components, Operations & Features ###

#### Components ####
##### PGraphComputer #####
This is used to start a Petra system and is invoked from the Java main method.
```java
package io.cognitionbox.petra.examples.simple.compose;


import io.cognitionbox.petra.lang.config.PetraConfig;
import io.cognitionbox.petra.lang.Void;
import io.cognitionbox.petra.lang.PGraphComputer;
import io.cognitionbox.petra.examples.simple.common.A;

public class PetraExample {
    public static void main(String[] args){

        // logs progression of tokens in places
        PGraphComputer.getConfig().enableStatesLogging();
        
        // The line below kicks of a Petra graph named DependancyGraph which takes A as input and
        // returns Void as output.
        Void result = new PGraphComputer<A, Void>().computeWithInput(new DependancyGraph(),new A());
        System.out.println(result);
    }
}
```
##### PGraph #####
A PGraphComputer must take as input a PGraph and the input to start with.
A PGraph is a special type of Petri-net which has a single place which can store
multiple tokens. A PGraph has a pre-condition and a post-condition.
The PGraph only starts if its pre-condition is met.
PGraph pre-conditions can be either read-only or read/consume.
read-only means the PGraph does not consume the input from its parent and
if the read-only pre-condition operates on a @Extract annotated type, then
a reference to the extracted type will remain after the extraction process.
In the case of the root graph kicked of by the PGraphComputer there is no place to consume
from so it does not make a difference to the PGraphComputer if either read-only or read/consume is used.
A PGraph can also have multiple steps and joins, where a step can be another PGraph or a PEdge, 
and a join is a special type of edge for combining values together.
When a another PGraph or PEdge becomes a step, or PJoin becomes a join, of a higher-level PGraph,
they all become children of that PGraph, and thus this PGraph is the parent of these children.
```java
package io.cognitionbox.petra.examples.simple.compose;

import io.cognitionbox.petra.lang.PGraph;
import io.cognitionbox.petra.examples.simple.common.*;



public class AtoC extends PGraph<A, C> {
    {
        pre(readConsume(A.class,a->true));
        step(new BtoC());
        step(new AtoB());
        post(returns(C.class,c->true));
    }
}
```

##### PEdge #####
PEdges are one of the finest unit of computation in the Petra system, along with PJoins, in that
they cannot be composed of any other Petra components.
PEdges support the following pre-conditions: read-only, read/write and read/consume.
These are the same as with the PGraph except for the addition of read/write with allows PEdges to
modify the state of a value within the parent's place.

```java
package io.cognitionbox.petra.examples.simple.compose;

import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.examples.simple.common.B;
import io.cognitionbox.petra.examples.simple.common.C;



public class BtoC extends PEdge<B,C> {
    {
       pre(readConsume(B.class,b->true));
       func(b->new C());
       post(returns(C.class,c->true));
    }
}
```

##### PJoin #####

PJoin is Petra's answer to joining data within a PGraph's place.

Within a PGraph, joins run sequentially in the order they are defined, 
after the steps have run. Joins require at least one match on all of their
multiple pre-conditions.

Joins use their pre-conditions to match all the instances in a PGraph's place and
bring these into the join's context so that can be operated on together.
This is useful for aggregations and/or combining objects.

Joins can only have one read/write pre-condition, (see read/write operation in section below),
and so the other pre-conditions must be read-only or read/consume in this case.
Also joins with a read/write pre-condition can only trigger if there is exactly one match for
its read/write pre-condition. 
read/write pre-condition joins must have a post-condition which is can be applied to 
the matched object.
This keeps programming with joins pure when dealing with side-effects as the post-condition 
is for checking the state change of the original instance that the read/write pre-condition matched on.

There are two main ways to use PJoins. Either by declaring the PJoin as a joinSome or joinAll.
joinSome requires some of the tokens in the PGraph's place to be matched, in order to trigger the PJoin.
joinAll requires all of the tokens in the PGraph's place to be matched, in order to trigger the PJoin.

```java
package io.cognitionbox.petra.examples.simple.join;

import io.cognitionbox.petra.lang.PJoin2;
import io.cognitionbox.petra.examples.simple.common.A;
import io.cognitionbox.petra.examples.simple.common.AB_Result;
import io.cognitionbox.petra.examples.simple.common.B;



public class ABtoABjoin extends PJoin2<A, B, AB_Result> {
    {
       preA(readConsume(A.class,x->true));
       preB(readConsume(B.class,x->true));
       func((as, bs)->{
            A a = as.get(0);
            B b = bs.get(0);
            return new AB_Result(a,b);
       });
       post(returns(AB_Result.class,x->true));
    }
}
```

##### Collections #####

Petra's PCollection's are designed to have the same behaviour across, 
sequential, parallel and distributed modes, without having to change the code.
So far there are 3 PCollection types: ```PList``` , ```PSet``` and ```PMap```
more collection types are coming soon.

You can also use Java collections or other library collections but without the guarantee 
semantics will stay consistent over all the runtime modes (sequential, parallel and distributed).

#### Operations ####
##### Pre-conditions #####
###### readOnly ######
Reads a value from the parents place, and does not consume it.
###### readWrite ######
Reads a value from the parents place, does not consume it, 
and allows writing to this value.
###### readConsume ######
Consumes a value from the parents place, for reading only.
##### Post-conditions #####
###### returns ######
Declares a value which will be returned to the parents place.
It can be used one or more times if there is a choice of values to return e.g.
```java
post(returns(Person.class,p->p.getHeightType().equals(HeightType.SHORT)));
post(returns(Person.class,p->p.getHeightType().equals(HeightType.MEDIUM));
post(returns(Person.class,p->p.getHeightType().equals(HeightType.TALL)));
```

For PGraph to successfully return an object, there must be only 1 object
in the graph's place and this object must match one of the returns post-conditions.
In the case of a PGraph which returns Void, there must be 0 objects in the graph's place.
Void types are interpreted as the lack of an object, i.e. steps which consume from
their parent and return Void, are purely consumers, and do not return another object back
into the parent's place.

For PEdge to successfully return an object it simply must output an object from its
function that matches one of the edge's returns post-conditions.

##### postVoid #####
Declares no value will be returned to the parents place.

##### @Extract #####
Objects of classes marked @Extract will be deconstructed into its constituents.
If the class is an iterable the constituents will be its elements.
If the class is not an iterable the mechanism will look for public methods which are 
marked @Extract and have no parameters and have a non void return type. 

The extract mechanism is invoked after the pre-condition (on PGraphs only) 
or post condition (on either PGraphs, PEdges or PJoins) is met.
If the pre-condition is on a PGraph the extract mechanism will deconstruct the values into the
PGraph's place. Extracts on post-conditions will extract the values into 
PGraph's / PEdge's / PJoin's, parent's place.

##### @Exclusive #####
In it's "strict mode" Petra does not allow non-primitive, static fields or non-final primitive static fields.
This is to reduce the probability of race-conditions and deadlocks.
In order to manage shared resources in an efficient and safe way Petra introduces the 
@Exclusive annotation to mark classes, interfaces and their public methods as @Exclusive.
Objects of @Exclusive types are created as singletons automatically by Petra.
For a PGraph, PEdge or PJoin to access an @Exclusive value no other PGraph, PEdge or PJoin
must be currently operating on it or any of its fields exposed by public @Exclusive methods.

##### step #####
A PGraph can have any number of steps which are either themselves PGraphs or PEdges.
```java
package io.cognitionbox.petra.examples.simple.compose;

import io.cognitionbox.petra.lang.PGraph;
import io.cognitionbox.petra.examples.simple.common.*;



public class AtoC extends PGraph<A, C> {
    {
        pre(readConsume(A.class,a->true));
        step(new BtoC());
        step(new AtoB());
        post(returns(C.class,c->true));
    }
}
```
##### joins #####
A PGraph can also have any number of PJoins.
Joins can either be joinSome or joinAll.

##### joinSome #####
joinSome requires some of the tokens in the PGraph's place to be matched, 
in order to trigger the PJoin.

```java
package io.cognitionbox.petra.examples.simple.forkjoin;

import io.cognitionbox.petra.lang.PGraph;
import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.examples.simple.common.*;

public class ABtoAB extends PGraph<AB, AB_Result> {
    {
        pre(readConsume(AB.class,x->true));
        step(new IncrementA());
        step(new IncrementB());
        joinSome(new ABtoABjoin());
        post(returns(AB_Result.class,x->true));
    }
}
```

##### joinAll #####
joinAll requires all of the tokens in the PGraph's place to be matched, 
in order to trigger the PJoin.

```java
package io.cognitionbox.petra.examples.simple.forkjoin;

import io.cognitionbox.petra.lang.PGraph;
import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.examples.simple.common.*;

public class ABtoAB extends PGraph<AB, AB_Result> {
    {
        pre(readConsume(AB.class,x->true));
        step(new IncrementA());
        step(new IncrementB());
        joinAll(new ABtoABjoin());
        post(returns(AB_Result.class,x->true));
    }
}
```

##### func #####
Each PEdge and PJoin must have a "func" which contains 
Java code which performs a computation.

#### Example Features ####

##### Object Extraction #####
See @Extract above.

##### Dynamic Step Parallelism #####
If there are multiple instances of a class in a place and there is a step which can
consume this type of instance, then Petra will dynamically create a step for each instance,
thus it scales with the data.

##### Transactional Memory #####
Petra ensures computations either complete successfully, or rollback to a state before the
computation started. This means it becomes easier to reason about a complex system as we always know
what sort of states the system can be in, rather than worrying about partial states.

##### Side-effect Safety #####
Only read/write pre-conditions can mutate the in-process memory state of the system.
This is true for sequential and parallel modes only, distributed mode is experimental and 
this feature might not be fully supported yet.
Future versions will add annotations to acquire specific system access to actions like writing files,
networking, and cloud access permissions in order to improve the control of side-effects. 

##### Automatic Retries #####
Petra is designed to roll back the changes made by a PEdge or PJoin if an error occurs which causes the 
computation to terminate early or if the post-condition is not met.
This means PEdge and PJoin computations will be automatically retried 
as their start state will be present again. 
This means developers no longer have to write explicit retry code.

##### Safe Shared Resources #####
See @Exclusive above.

##### Construction Checks #####

Petra includes several construction checks which are on by default.
These checks prevent a Petra system from running if one of the checks fail.
The checks enforce the semantics of the Petra system to provide safety guarantees.
If there are multiple issues that prevent start-up all of these are logged to the console.
Some checks cannot be run unless critical checks pass e.g. all steps must have
pre/post conditions. With out these it is impossible to perform the other checks.

Some example checks are explained below.

###### Type Predicate bindings ######
Petra pre/post conditions use Java class and interface types to match instances of the defined type.
Further to this once if the instance matches the defined type a predicate of the same same is evaluated on the 
instance which provides a more refined/stronger check on the instance.
In order to ensure the same type is not used with multiple predicates Petra has a system for checking this upfront,
and will cause the startup to fail so the issue can be fixed.

###### Reachability Disproover ######
Petra uses the class / interface type information in the pre/post conditions to perform reachability analysis on 
PGraphs and can show it is impossible to reach the post-condition of a PGraph.
If this check pass its still possible for the PGraph to not reach the post-condition, this check is for showing
cases where it is impossible, given the type information, to reach the post-condition of a PGraph.

###### Construction Check documentation ######
This is not complete but needs to be completed as a priority so new users
can better understand why start-up is being prevented.

##### Petra DOT state flow diagrams #####

If all checks pass a DOT diagram text output will be logged to the console.
This diagram is Petra State flow diagram which provides a visual state machine 
type view of a Petra system.

To view the diagram copy and paste the logged DOT code to an online DOT renderer:

https://dreampuf.github.io/GraphvizOnline OR
http://www.webgraphviz.com/

##### JVM language support #####

###### Java 8 ######
Petra has been written in Java 8 as an embedded DSL and therefore
Java 8 support is native to Petra.

###### Kotlin ######
Kotlin works seamlessly with Petra for the same reason as above.
A petra-kotlin library will be released soon which will further 
improve the convenience of using Kotlin with Petra.

### Who do I talk to? ###

The idea of Petra was originally thought up by Aran Hakki in 2011.
Since then he has been researching and developing the system.

Aran originally read MEng Systems Engineering at Warwick University 
and has been a software engineer for about 10 years now.
He will be starting a PhD in Computer Science in May 2020 and the 
main research topics will be those surrounding the Petra programming system.

If you are interested in this project and believe you could benefit 
from this technology please get in touch.

* Aran Hakki
* aran@cognitionbox.io
* +44(0)7399472347