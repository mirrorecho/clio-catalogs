(

ClioLibrary.catalog ([\func, \fx], {


	~busIn = ClioSynthFunc({ arg kwargs;
		kwargs[\synth][\sig] = kwargs[\synth][\sig] + In.ar(kwargs[\bus], kwargs[\numChannels]);

	}, *[ // set default kwargs:
		numChannels:2,
		bus:nil,
	]);

	// =====================================================================================

	// TO DO: create reverb using FreeVerb

	// sigs don't need same kwargs for these, such as freq and amp
	~reverb2 = ClioSynthFunc({ arg kwargs;

		var sig = FreeVerb2.ar(kwargs[\synth][\sig][0], kwargs[\synth][\sig][1],
			mix:kwargs[\mix],
			room:kwargs[\room],
			damp:kwargs[\damp],
		);

		kwargs[\synth][\sig] = sig;

	}, *[ // defaults:
		mix: 0.66,
		room:0.5,
		damp:0.5,
	]);

	// =====================================================================================

	~echo = ClioSynthFunc({ arg kwargs;

		kwargs[\synth][\sig] = CombL.ar( kwargs[\synth][\sig],
			maxdelaytime:kwargs[\delaytime],
			delaytime:kwargs[\delaytime],
			decaytime:kwargs[\decaytime],
			mul:kwargs[\mul],
		add:kwargs[\synth][\sig]);

	}, *[ // defaults:
		delaytime:0.5,
		decaytime:4,
		mul:0.4,
	]);

	// =====================================================================================

	~distortion = ClioSynthFunc({ arg kwargs;

		// a pretty good distortion algorithm:
		var sigDistort = (kwargs[\synth][\sig] * (3 + (kwargs[\distortion] * 40))).distort * (1-(kwargs[\distortion]/1.4)) * 0.4;

		kwargs[\synth][\sig] = (kwargs[\synth][\sig]*(1-kwargs[\mix])) + (sigDistort*kwargs[\mix]);

		}, *[
			distortion:0.4,
			mix:0.66,
	]);

	// =====================================================================================

	// TO DO: ringing fx

	// =====================================================================================

}, { |envir|
	// add any code below that should ONLY execute if executing through interpreter here.

}, { |envir|
	// add any code below that should ONLY execute if catalog is being loaded through ClioLribrary.
});

)




