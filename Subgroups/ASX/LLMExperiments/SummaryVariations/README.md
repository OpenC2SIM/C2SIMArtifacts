# LLM Paper Summary Variations

One of the most powerful capabilities of LLMs is their ability to reshape results based on further guidance, which is ten incorporated into an ongoing context. Given a result, one can for example direct the model to “make it shorter”, “make this sound more friendly (or more professional)”, or “write this as a limerick” .  Here, we were interested in exploring how this could be used to produce variations of the scenarios that were extracted from papers, exploring alternatives and multiplying the number of collected scenarios with the model’s aid. 

We examined variations via prompts provided to GPT-4 immediately after the production of successful paper summary extractions.Files contained in this folder show the results of some of these variations for illustrative purposes.
Each of the variations is built on top of the previous one, in incremental chained transformations that takes the ongoing context into consideration.

1. Change of settings via a [`Change this to a tropical forest setting` prompt](./TropicalForrest.md).

1. Change of type of asset via a [`Use just UGVs`prompt](./AssetTypeVariations.md).
1. Ask for five different synthetic scenarios via a [`Generate 5 very distinct variations of the scenario. There are no restrictions on the type and number of autonomous system employed` prompt](./FiveAlternatives).

**NOTE**: GPT-4 runs with default parameters may generate different results for the same inputs. The files contained in here represent one of these runs, evaluated as having successfully met the evaluation criteria to a large extent. 

