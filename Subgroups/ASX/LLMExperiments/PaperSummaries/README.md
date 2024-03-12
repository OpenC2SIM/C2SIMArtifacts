# LLM Paper Summaries


We first examined LLMs capabilities as a “smart formatter”, to aid in extracting standard scenario descriptions from closely related literature contributed by the sub-group members:


1. Corona, F. & Biaginni, M.  (2019) C2SIM Operationalization Extended to Autonomous Systems. Modelling and Simulation for Autonomous Systems. MESAS 2019. https://doi.org/10.1007/978-3-030-43890-6_32

1. Biaginni, M., Corona, F., Innocenti, F., Marcovaldi, S. (2018) Requirements and Example for a C2SIM Extension to Unmanned Autonomous Systems (UAXS). NATO M&S COE Annual Review Vol. 2, Chapter 8, 2018, p93
1. Brutzman, D.P., Fitzpatgrick, C.R. (2020) Creating Virtual Environments for Evaluating Human-Machine Teaming, Naval Postgraduate School Technical Report NPS-MV-20-001. https://apps.dtic.mil/sti/pdfs/AD1127315.pdf
1. Langerwisch, M., Wittmann, T., Thamke, S., Remmersmann, T. , Tiderko, A. & Wagner, B. (2013) Heterogeneous teams of unmanned ground and aerial robots for reconnaissance and surveillance - A field experiment. 2013 IEEE International Symposium on Safety, Security, and Rescue Robotics, SSRR 2013. 
1. Remmersmann, T, Schade, U., Rein, K. & Tiderko, A. (2015). BML for Communicating with Multi-Robot Systems. Fall Simulation Interoperability Workshop, Fall SIW 2015
1. Remmersmann, T., Trautwein, I., Schade, U., Brüggemann, B., Lassen, C., Westhoven, Martin & Wolski, M. (2016) Towards Duty – BML Communication Enables a Multi-Robot System Supporting an Infantry Platoon. SISO 2016 Fall Simulation Innovation Workshop. https://publica.fraunhofer.de/handle/publica/400870

Corona & Biagini (2019) and Biagini et al. (2018) were of particular interest, because of their focus on the specific problem the sub-group is working on – extending C2SIM to include the representation of autonomous systems. Their approach is based on the analysis of scenarios as the basis for the definition of required extensions to the C2SIM ontology. The extensions identified by these authors are a starting point for the current effort. 

Files in this folder present GPT-4 results when each of the papers was processed (as attachment to a Playground Assistant) using the prompt describe in the next section.


**NOTE**: GPT-4 runs with default parameters may generate different results for the same inputs. The files contained in here represent one of these runs, evaluated as having successfully met the evaluation criteria to a large extent. 

## Prompt evolution and evaluation

Prompts were iteratively refined to overcome perceived flaws and inconsistencies of the results. Since different responses may be returned by GPT-4 for runs with the same input, each version was evaluated considering five runs. Once results started to be considered adequate, prompt versions were used to process the remaining papers of the set, to verify their generality, and avoid the risk of overfitting the prompt, i.e., making it too specific to a particular paper.

The following questions were considered during evaluation, rated in a scale from 0 to 5:
- Does the result contain all elements of the standard scenario template? Evaluates whether there are omitted (or added) elements that deviate from the desired outcome.
- Do the results reflect just the scenarios contained in the paper? Evaluates the success in extracting the scenarios from the papers’ broader consideration and discussion. In many cases the scenarios are just a small part of the overall papers and may be used only for illustrative purposes.
- Is the Description a fair summary of the paper scenario? Evaluates how closely the generated summary matches the “gist” of the paper descriptions and does not contain fantasized details (“hallucinations”). 
- Are the generated Goals and Measurements of Performance reasonable given the context?  
- Are the Steps a logical temporal progression that reflect the scenario? 



## Prompt

The current version of the prompt (detailed below) achieved consistent results that matched the qualitative criteria.
Results vary per run, even when provided with the same input under the standard GPT-4 Assistant parameters, which allow for variations. Since there is no single expected canonical result, the variation is a feature useful for exploration.

Current prompts display an occasional tendency to present results that summarize the content of the paper as a whole, rather than focus narrowly on the specific scenarios they contain. 
That is particularly true when the scenarios are just a minor part of a paper concerned with higher level discussions.

An example of a run in which the paper is described, rather than the scenario can be found [here](./CounterExample.md).

```
Persona 
=======
You are an autonomous systems researcher describing with accuracy military scenarios contained in technical papers.

Input
=====
Extract the scenario details described in the attached file. Focus on the illustrative scenarios themselves and their goals, rather than the broader description of the paper itself.
 
Output
======
Format your description according to the following items:
-	Summary narrative 
-	Specific goals to be achieved by the systems given the objectives 
-	Measures of Performance 
-	Scenario steps - these should not be vague descriptions, but actual steps to achieve the objectives given the context over time. 

Conceptual Model
================
Use the instructions within triple quotes (""") below to extract an ontology from this scenario. List the results in a section labeled "Conceptual Model"
"""
- Identification of the Domain and Scope: The first step in extracting ontology from a scenario requires the definition of the domain and scope that this ontology will cover. This involves identifying and specifying the general areas and the specific details respectively.

- Concept Identification: Analyze the collected data to identify the main concepts in the domain. Concepts are typically represented as classes in ontology.

- Define the Hierarchy: Structure the concepts based on their dependence or inheritance relationships with each other to design a class hierarchy. This often ends up looking like a tree with "parent" concepts (superclasses) and "children" concepts (subclasses).

- Relationship Identification: Identify the relationships between the concepts. There may be different types of relationships between the concepts, including IS-A relationships (e.g. a dog IS-A pet), part-whole relationships (e.g. a wheel is part of a car), etc.

- Property Identification: Identify properties (or attributes) of each concept. For example, a car may have properties like color, model, make, etc.
Identify the Constraints/Rules: Identify any existing constraints or rules within the context of your scenario that apply to the properties or relationships of the classes. These rules govern the behavior of the classes and their relationships. """
```



 
