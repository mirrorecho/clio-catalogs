(

ClioLibrary.catalog ([\func, \ringer], {

	// TO DO: this is almost the same as ~ringer below. DRY?
	~overtone = ClioSynthFunc({ arg kwargs;

		var specsArray = [
			kwargs[\overtones] * kwargs[\synth][\freq],
			kwargs[\overtoneAmps] * kwargs[\ampScale] * 0.6,
			kwargs[\ringTimes]
		];

		var ringSig = kwargs[\oscType].ar(`specsArray, kwargs[\synth][\sig]) * AmpComp.kr( kwargs[\synth][\freq], 20, 0.2);

		kwargs[\synth][\sig] = (ringSig * kwargs[\mix]) + (kwargs[\synth][\sig] * (1-kwargs[\mix]));

	}, *[ // kwarg defaults:
		oscType:Klank, // can replace Klank with DynKlank to make it dynamic
		overtones:[1, 2, 3, 4, 5, 6, 7, 8],
		overtoneAmps:[0.8, 0.7, 0.6, 0.5, 0.4, 0.3, 0.2, 0.1],
		ringTimes:[0.8, 0.7, 0.6, 0.5, 0.4, 0.3, 0.2, 0.1],
		mix:0.9,
		ampScale:1,
	]);


	~ringer = ClioSynthFunc({ arg kwargs;

		var specsArray = [
			kwargs[\ringFreqs],
			kwargs[\ringAmps] * kwargs[\ampScale],
			kwargs[\ringTimes],
		];

		var ringSig = kwargs[\oscType].ar(`specsArray, kwargs[\synth][\sig]);

		kwargs[\synth][\sig] = (ringSig * kwargs[\mix]) + (kwargs[\synth][\sig] * (1-kwargs[\mix]));

	}, *[ // defaults:
		oscType:Klank, // can replace Klank with DynKlank to make it dynamic
		ringFreqs:[440, 880],
		ringAmps:[0.8, 0.6],
		ringTimes:[1, 0.6],
		mix:0.9,
		ampScale:1,
	]);


	~ringCymbal = ClioSynthFunc({ arg kwargs;

		var ringCount = kwargs[\ringCount];

		var specsArray = [
			{ExpRand(kwargs[\loFreq], kwargs[\hiFreq])}!ringCount, // freqs
			(1/ringCount * kwargs[\ampScale])!ringCount, // amps
			Rand(0.1!ringCount,1!ringCount), // ring times
		];

		var ringSig = kwargs[\oscType].ar(`specsArray, kwargs[\synth][\sig]);


		kwargs[\synth][\sig] = (ringSig * kwargs[\mix]) + (kwargs[\synth][\sig] * (1-kwargs[\mix]));

	}, *[ // defaults:
		oscType:Klank, // can replace Klank with DynKlank to make it dynamic
		ringCount:40,
		loFreq:290,
		hiFreq:12000,
		mix:0.9,
		ampScale:1,
	]);


	// TO DO, a ringer with DynKlank


});



)




