package fr.ina.research.amalia.model;

import fr.ina.research.amalia.AmaliaException;
import fr.ina.research.amalia.model.MetadataBlock.MetadataType;
import fr.ina.research.rex.commons.tc.RexTimeCode;

public class TestMetadataFactory {

	public static void main(String[] args) {
		try {
			MetadataBlock metadata = MetadataFactory.createMetadataBlock("test-123456", MetadataType.DETECTION);
			metadata.setAlgorithm(TestMetadataFactory.class.getSimpleName());
			metadata.setProcessor("Ina Research Department - N. HERVE");
			LocalisationBlock root = metadata.addLocalisationBlock(RexTimeCode.build(0, 0, 0, 0), RexTimeCode.build(0, 1, 0, 0));
			root.addLocalisationBlock(RexTimeCode.build(0, 0, 30, 0)).setLabel("A label at 30 sec");
			
			System.out.println("<!-- XML serialization -->");
			System.out.println(MetadataFactory.serializeToXMLString(metadata));
			System.out.println();
			System.out.println("<!-- Json serialization -->");
			System.out.println(MetadataFactory.serializeToJsonString(metadata));
			
		} catch (AmaliaException e) {
			e.printStackTrace();
		}

	}

}
