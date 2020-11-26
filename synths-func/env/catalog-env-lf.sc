(

ClioLibrary.catalog ([\func, \env, \lf], {

	~pulse = ClioSynthFunc({ arg kwargs;

		var lf_sig = kwargs[\oscType].kr(
			freq:kwargs[\oscFreq],
			width:kwargs[\oscWidth]
		);

		kwargs[\synth][\sig] = kwargs[\synth][\sig] * lf_sig;

	}, *[ // sets default kwargs:
		oscType:LFPulse,
		oscFreq:1,
		oscWidth:0.5,
	]);

	// =====================================================================================

}, { arg key, env;
	// f = env.asDict[\perc];
	// env[\perc].();
});


)
