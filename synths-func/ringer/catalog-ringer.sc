(

ClioLibrary.catalog ([\func, \ringer], {

	// TO DO: this is almost the same as ~ringer below. DRY?
	~overtone = ClioSynthFunc({ arg kwargs;

		var specsArray = [
			kwargs[\overtones] * kwargs[\synth][\freq],
			kwargs[\overtoneAmps] * kwargs[\ampScale] * 0.6,
			kwargs[\ringTimes]
		];

		var ringSig = Klank.ar(`specsArray, kwargs[\synth][\sig]) * AmpComp.kr( kwargs[\synth][\freq], 20, 0.2);

		kwargs[\synth][\sig] = (ringSig * kwargs[\mix]) + (kwargs[\synth][\sig] * (1-kwargs[\mix]));

	}, *[ // kwarg defaults:
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

		var ringSig = Klank.ar(`specsArray, kwargs[\synth][\sig]);

		kwargs[\synth][\sig] = (ringSig * kwargs[\mix]) + (kwargs[\synth][\sig] * (1-kwargs[\mix]));

	}, *[ // defaults:
		ringFreqs:[440, 880],
		ringAmps:[0.8, 0.6],
		ringTimes:[1, 0.6],
		mix:0.9,
		ampScale:1,
	]);

	// TO DO, a ringer with DynKlank


});



)




