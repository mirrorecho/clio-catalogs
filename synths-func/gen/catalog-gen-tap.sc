(

ClioLibrary.catalog ([\func, \gen, \tap], {

	// NOTE: needs DetectSilence
	~tap = ClioSynthFunc({ arg kwargs;
		var sig;

		sig = Resonz.ar(
			kwargs[\inOscType].ar(70!2) * Decay2.kr( Trig.kr(kwargs[\synth][\amp], 0.01), 0.002, 0.1 ),
			kwargs[\synth][\freq],
			0.01,
			6
		).distort * 0.3;
		kwargs[\synth][\sig] =  kwargs[\synth][\sig] + sig;

	}, *[
		inOscType:WhiteNoise,
		rq: 0.008,
		ampMul:1
	]);


	// nice rhythmic synth
	// NOTE: needs an envelope
	~tappy = ClioSynthFunc({ arg kwargs;
		var sig;

		sig = SinOscFB.ar(
			kwargs[\synth][\freq],
			kwargs[\feedbackOscType].ar(kwargs[\feedbackOscRate]!2)+1)/9;

		kwargs[\synth][\sig] =  kwargs[\synth][\sig] + sig;

	},*[
		feedbackOscType:Saw,
		feedbackOscRate:8,
	]);




}, { |envir|
	// add any code below that should ONLY execute if executing through interpreter here.

}, { |envir|
	// add any code below that should ONLY execute if catalog is being loaded through ClioLribrary.
});

)
