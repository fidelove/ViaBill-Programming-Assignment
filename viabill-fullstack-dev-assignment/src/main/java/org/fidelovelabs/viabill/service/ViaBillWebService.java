package org.fidelovelabs.viabill.service;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.options;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.SparkBase.port;

import java.util.Map;

import org.fidelovelabs.viabill.handler.AddOwnerRequestHandler;
import org.fidelovelabs.viabill.handler.BeforeRequestHandler;
import org.fidelovelabs.viabill.handler.CreateCompanyRequestHandler;
import org.fidelovelabs.viabill.handler.GetCompaniesRequestHandler;
import org.fidelovelabs.viabill.handler.GetCompanyDetailsRequestHandler;
import org.fidelovelabs.viabill.handler.OptionsRequestHandler;
import org.fidelovelabs.viabill.handler.UpdateCompanyDetailsRequestHandler;
import org.fidelovelabs.viabill.model.CompanyBean;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IAtomicLong;

public class ViaBillWebService {

	public static void main(String[] args) {
		Config config = new Config();
		HazelcastInstance instance = Hazelcast.newHazelcastInstance(config);
		Map<Long, CompanyBean> mapCompanies = instance.getMap("companies");
		IAtomicLong idCompany = instance.getAtomicLong("idCompany");

		port(9090);

		post("/createCompany", new CreateCompanyRequestHandler(mapCompanies, idCompany));
		get("/getCompanies", new GetCompaniesRequestHandler(mapCompanies));
		get("/getCompany/:idcompany", new GetCompanyDetailsRequestHandler(mapCompanies));
		put("/updateCompany/:idcompany", new UpdateCompanyDetailsRequestHandler(mapCompanies));
		put("/addOwner/:idcompany", new AddOwnerRequestHandler(mapCompanies));

		// CORS habilitation
		options("/*", new OptionsRequestHandler());
		before(new BeforeRequestHandler());
	}
}