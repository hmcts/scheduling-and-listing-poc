package uk.gov.hmcts.reform.sandl.model.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import uk.gov.hmcts.reform.sandl.engine.RulesEngine;
import uk.gov.hmcts.reform.sandl.model.common.Identified;
import uk.gov.hmcts.reform.sandl.model.jurisdiction.CaseType;
import uk.gov.hmcts.reform.sandl.model.jurisdiction.HearingRole;
import uk.gov.hmcts.reform.sandl.model.jurisdiction.HearingType;
import uk.gov.hmcts.reform.sandl.model.jurisdiction.Jurisdiction;
import uk.gov.hmcts.reform.sandl.model.jurisdiction.Service;
import uk.gov.hmcts.reform.sandl.model.person.Person;
import uk.gov.hmcts.reform.sandl.model.person.Qualification;
import uk.gov.hmcts.reform.sandl.model.schedule.Available;
import uk.gov.hmcts.reform.sandl.model.schedule.NotAvailable;
import uk.gov.hmcts.reform.sandl.model.venue.Cluster;
import uk.gov.hmcts.reform.sandl.model.venue.Region;
import uk.gov.hmcts.reform.sandl.model.venue.Room;
import uk.gov.hmcts.reform.sandl.model.venue.Venue;

public class CSVIO
{
	public static class FileToClass
	{
		public final String fileName;
		public final Class<? extends Identified> clazz;
		public FileToClass(String fileName, Class<? extends Identified> clazz)
		{
			this.fileName = fileName;
			this.clazz = clazz;
		}
	}

	public static FileToClass map(String fileName, Class<? extends Identified> clazz)
	{
		return new FileToClass(fileName, clazz);
	}

	public static final FileToClass[] FILE_TO_CLASS = new FileToClass[]
			{
					map("jurisdiction.csv", Jurisdiction.class),
					map("service.csv", Service.class),
					map("case-type.csv", CaseType.class),
					map("hearing-type.csv", HearingType.class),
					map("hearing-role.csv", HearingRole.class),
					map("person.csv", Person.class),
					map("qualification.csv", Qualification.class),
					map("region.csv", Region.class),
					map("cluster.csv", Cluster.class),
					map("venue.csv", Venue.class),
					map("room.csv", Room.class),
					map("available.csv", Available.class),
					map("not-available.csv", NotAvailable.class),
			};

	private final RulesEngine rulesEngine;

	public CSVIO(RulesEngine rulesEngine)
	{
		this.rulesEngine = rulesEngine;
	}

	public void loadAll(File directory) throws IOException
	{
		for (FileToClass fileToClass : FILE_TO_CLASS)
		{
			load(directory, fileToClass.fileName, fileToClass.clazz);
		}
	}

	public void load(File directory, String fileName, Class<? extends Identified> clazz) throws IOException
	{
		System.out.println("Loading " + clazz.getName());
		try (Reader reader = new FileReader(new File(directory, fileName)))
		{
			load(reader, clazz);
		}
	}

	public void load(Reader reader, Class<? extends Identified> clazz) throws IOException
	{
		List<Identified> facts = loadFacts(reader, clazz);
		rulesEngine.processTransaction(facts);
	}

	public List<Identified> loadFacts(Reader reader, Class<? extends Identified> clazz) throws IOException
	{
		List<Identified> values = new ArrayList<>();
		CsvMapper csvMapper = new CsvMapper();
		csvMapper.findAndRegisterModules();
		csvMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		CsvSchema csvSchema = csvMapper.schemaFor(clazz).withUseHeader(true);
		MappingIterator<Identified> iterator = csvMapper.readerFor(clazz).with(csvSchema).readValues(reader);
		while (iterator.hasNextValue())
		{
			values.add(iterator.nextValue());
		}
		return values;
//		return new CsvToBeanBuilder<T>(reader).withType(clazz).build().parse();
	}

	public void storeAll(File directory) throws IOException
	{
		for (FileToClass fileToClass : FILE_TO_CLASS)
		{
			Collection<Identified> objects = rulesEngine.getStatedFacts(Identified.class, o -> fileToClass.clazz.isAssignableFrom(o.getClass()));
			store(directory, fileToClass.fileName, fileToClass.clazz, objects);
		}
	}

	public void store(File directory, String fileName, Class<?> clazz, Collection<?> objects) throws IOException
	{
		directory.mkdirs();
		CsvMapper csvMapper = new CsvMapper();
		csvMapper.findAndRegisterModules();
		csvMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		CsvSchema csvSchema = csvMapper.schemaFor(clazz).withUseHeader(true);
		try (Writer writer = new FileWriter(new File(directory, fileName)))
		{
			csvMapper.writer(csvSchema).writeValue(writer, objects);
		}
	}
}
