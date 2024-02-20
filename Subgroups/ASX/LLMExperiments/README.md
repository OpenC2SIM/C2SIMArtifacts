# C2SIM Autonomous Systems Extension - ASX
## Large Language Model experiments

As part of this effort, the group started an investigation into the capabilities of AI tools to facilitate crafting of scenarios. In particular, there was interest in exploring to what degree Large Language Models (LLMs) could contribute to the task. This repository contains artifacts relates to this effort.

Please see the References for a discussion of the approach.

# Development environment

Development of prompts followed an iterative cycle where prompts were incrementally refined to address issues detected during evaluation. Development was done within OpenAI’s Playground, a simple but effective web-based environment for defining and testing prompts. The Playground interface was used to define “Assistants” containing prompt instructions. All Assistants used the `gpt-4-turbo-preview` model.

Manually developed prompts were processed through a meta-prompt, which instructed GPT-4 to use OpenAI's own [Prompt Engineering best practices guide](https://platform.openai.com/docs/guides/prompt-engineering) to improve the prompt.

See [Meta prompt](./MetaPrompt/) for details.



## Experiments

1. [Paper Summaries](./PaperSummaries) - The initial task was the extraction of standardized scenario descriptions and details from related technical papers. Prompts were evolved primarily based on this tasks.
1. [Summary Variations](./SummaryVariations) - Intrinsic linguistic capabilities of LLMs were then explored to general context-appropriate variations of the scenarios extracted from papers. These derived versions incorporate varying degrees of domain knowledge encoded in the LLM model.
1. [Synthetic Scenarios](./SyntheticScenarios/) - Finally, encoded domain knowledge is exploited more directly in experiments that generate synthetic scenarios from user provided `Goals` and optional `Context`. A pipeline of Assistants that 1) extract model knowledge related autonomous systems's `Areas` of employment, and  2) obtain model-produced `Goals` given an `Area` provide means for fully-synthetic scenarios.


## References

Barthelmess, P. & Blais C.L. (2024). Exploring Large Language Models for Scenario Generation in Support of C2SIM Autonomous Systems Ontology Extension. Simulation Interoperability Workshop, SIW 2024.
- [Paper](./References/2024-SIW-Presentation-16.pdf)
- [Presentation](./References/2024-SIW-Presentation-16-ppt.pdf)