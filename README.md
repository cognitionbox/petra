# petra #

[![Apache 2.0](https://img.shields.io/badge/License-Apache%202.0-brightgreen?style=shield)](https://www.apache.org/licenses/LICENSE-2.0) [![<CognitionBox>](https://circleci.com/gh/cognitionbox/petra.svg?style=shield)](https://circleci.com/gh/cognitionbox) [![contributions welcome](https://img.shields.io/badge/contributions-welcome-brightgreen.svg?style=shield)](https://github.com/cognitionbox/petra)

## Overview ##

### What is Petra? ###

Petra is a concise hybrid functional and object-oriented 
general purpose programming language which aims to make 
parallel and distributed programs easier to code and verify.

Petra = Automatic Parallel/Distributed programming + 
            Programming with state made easy + 
                    Software Verification                              
                    
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
Petra has parallel and distributed programming built into its core and a program can be executed in 
sequential, parallel or distributed modes, whilst maintaining consistent semantics.

### What is this repository for? ###

This repo contains the following projects (please note the distributed implementation requires either of the hazelcast projects below, however both implementations are experimental and are likely to be unstable): 

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
When an Petra application starts the root iteration loop causes tasks to be added to Petra's task ring buffer.
All nodes are able to process tasks from the ring buffer. During processing of a task more tasks can be added to the ring buffer.
Tasks are never removed from the ring buffer but they are marked as complete once successfully completed.

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

Petra's two main programming components, PGraph, PEdge (see below)
are analogous to pure functions, even when they mutating state i.e. causing a side-effect.
This means a complex system can be composed of a hierarchy of function calls, 
whilst being able to mutate state in a safe and pure way.

Petra aims to parallelize at all opportunities whilst removing the
developers overhead to hand code parallel algorithms using traditional
concurrency primitives like threads, locks and fork/joins, etc.
Multiple PGraph and PEdge steps run in parallel where possible given their data dependencies.   

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
##### PComputer #####
This is used to start a Petra system and is invoked from the Java main method.
```java
public class PetraExample {
    public static void main(String[] args){

        // logs progression of tokens in places
        PComputer.getConfig().enableStatesLogging();
        
        // The line below kicks off a Petra graph named AtoAGraph which takes A as input and
        // returns A as output.

        A output = new PComputer<A>().eval(new AtoAGraph(),new A(""));
        System.out.println("OUTPUT: "+output.value);

    }
}
```
##### PGraph #####
A PComputer must take as input a PGraph and the input to start with.
A PGraph has a pre-condition and a post-condition.
The PGraph only starts if its pre-condition is met.
A PGraph can also have multiple steps, where each step can be another PGraph or a PEdge.
When a another PGraph or PEdge becomes a step of a higher-level PGraph,
they all become children of that PGraph, and thus this PGraph is the parent of these children.
```java
public class AtoAGraph extends PGraph<A> {
    {
       type(A.class);
       pre(a->a.value.equals(""));
       step(new AtoA());
       post(a->a.value.equals("hello world."));
    }
}
```

##### PEdge #####
PEdges are the finest unit of computation in the Petra system, in that
they cannot be composed of any other Petra components.

```java
public class AtoA extends PEdge<A> {
    {
       type(A.class);
       pre(a->a.value.equals(""));
       func(a->{
           a.value = "hello world.";
       });
       post(a->a.value.equals("hello world."));
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
##### Post-conditions #####
##### step #####
A PGraph can have any number of steps which are either themselves PGraphs or PEdges.
```java
public class SeqGraph extends PGraph<X> {
            {
                type(X.class);
                pre(x->x.isAB());
                step(new SeqEdge2());
                step(new SeqEdge1());
                post(x->x.isC());
            }
        }
```

##### func #####
Each PEdge must have a "func" which contains 
Java code which performs a computation.

#### Example Features ####

##### Dynamic Step Parallelism #####
If there are multiple instances of a class in a place and there is a step which can
consume this type of instance, then Petra will dynamically create a step for each instance,
thus it scales with the data.

##### Transactional Memory #####
Petra ensures computations either complete successfully, or rollback to a state before the
computation started. This means it becomes easier to reason about a complex system as we always know
what sort of states the system can be in, rather than worrying about partial states. 

##### Automatic Retries #####
Petra is designed to roll back the changes made by a PEdge if an error occurs which causes the 
computation to terminate early or if the post-condition is not met.
This means PEdge computations will be automatically retried 
as their start state will be present again. 
This means developers no longer have to write explicit retry code.

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

###### Construction Check documentation ######
This is not complete but needs to be completed as a priority so new users
can better understand why start-up is being prevented.

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
He started a PhD in Computer Science at the University of Southampton 
in May 2020 and the main research topics are those surrounding the Petra programming system.

If you are interested in this project and believe you could benefit 
from this technology please get in touch.

* Aran Hakki
* a.hakki@soton.ac.uk
* aran.hakki@gmail.com
* +44(0)7399472347