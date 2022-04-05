package de.fraunhofer.fkie.xsd.cli;

import de.fraunhofer.fkie.xsd.XsdCreator;
import picocli.CommandLine;

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;

@CommandLine.Command(mixinStandardHelpOptions = true)
public class XsdCreatorCli implements Callable<Integer> {

	@CommandLine.Parameters(index = "2", description = "Owl input file")
	private List<String> input;

	@CommandLine.Parameters(index = "0", description = "Output containing the generated schema")
	private File xsdOutput;
	@CommandLine.Parameters(index = "1", description = "The Merged OWL file")
	private File owlOutput;
	@CommandLine.Option(names = "--reasoning", description = "If reasoning shall be done; default false")
	private boolean doReasoning;

	@Override
	public Integer call() throws Exception {
		XsdCreator xsdCreator = new XsdCreator(input, owlOutput, doReasoning);
		xsdCreator.createSchema(xsdOutput.getAbsolutePath());
		return 0;
	}

	public static void main(String[] args) {
		int exitCode = new CommandLine(new XsdCreatorCli()).execute(args);
		System.exit(exitCode);
	}
}
