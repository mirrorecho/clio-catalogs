(

ClioLibrary.catalog ([\func, \buf], {


	// scales the rate of playback based on ratio of freq to a pre-defined centerFreq
	// HOW COOL!
	~rateScale = ClioSynthFunc({ arg kwargs;

		kwargs[\rate] * ( (kwargs[\synth][\freq] / kwargs[\centerFreq]) ** kwargs[\rateScale]);

	}, *[ // set defaults kwargs:
		rateScale:0.5,
		centerFreq:440,
		rate:1,
	]);

	// =====================================================================================

	~play = ClioSynthFunc({ arg kwargs;

		kwargs[\synth][\sig] = kwargs[\synth][\sig] + PlayBuf.ar( kwargs[\numChannels],
			bufnum:kwargs[\bufnum],
			rate:BufRateScale.kr(kwargs[\bufnum]) * kwargs[\rate],
			startPos:BufSampleRate.ir(kwargs[\bufnum]) * kwargs[\start],
			doneAction:kwargs[\doneAction],
		);

	}, *[ // set default kwargs:
		doneAction:2,
		numChannels:2,
		bufnum:nil, // should be defined when factory created
		start:0,
		rate:1,
	]);

	// =====================================================================================

	~stereoFloat = ClioSynthFunc({ arg kwargs;

		kwargs[\synth][\sig] = kwargs[\synth][\sig] + [PlayBuf.ar(1,
			bufnum:kwargs[\bufnum],
			rate:BufRateScale.kr(kwargs[\bufnum]) * kwargs[\rate],
			startPos:BufSampleRate.ir(kwargs[\bufnum]) * kwargs[\start],
			doneAction:kwargs[\doneAction],
		),
		PlayBuf.ar(1,
			bufnum:kwargs[\bufnum],
			rate:BufRateScale.kr(kwargs[\bufnum]) * kwargs[\rate]*kwargs[\floatRateMul],
			startPos:BufSampleRate.ir(kwargs[\bufnum]) * kwargs[\start],
			doneAction:0,
		)
		];

	}, *[ // set default kwargs:
		doneAction:2,
		bufnum:nil, // should be defined when factory created
		start:0,
		rate:1,
		floatRateMul:1.01,
	]);

	// =====================================================================================

	~drone = ClioSynthFunc({ arg kwargs;

		var bufsigs, bufenvs;
		var length = BufDur.ir(kwargs[\bufnum]);
		var envTimes = (length / kwargs[\rate] / 4)!3;

		bufsigs = 4.collect{ |i|
			PlayBuf.ar( kwargs[\numChannels],
				bufnum: kwargs[\bufnum],
				rate:BufRateScale.kr(kwargs[\bufnum]) * kwargs[\rate],
				startPos:BufSampleRate.ir(kwargs[\bufnum]) * length * i/4,
			loop:1);
		};

		bufenvs = [
			EnvGen.kr(Env.new(levels: [0, 0.25, 0.5, 0.25, 0], times: envTimes, curve: [6,-6,6,-6,]).circle),
			EnvGen.kr(Env.new(levels: [0.25, 0.5, 0.25, 0, 0.25], times: envTimes, curve: [-6,6,-6,6]).circle),
			EnvGen.kr(Env.new(levels: [0.5, 0.25, 0, 0.25, 0.5], times: envTimes, curve: [6,-6,6,-6]).circle),
			EnvGen.kr(Env.new(levels: [0.25, 0, 0.25, 0.5, 0.25], times: envTimes, curve: [-6,6,-6, 6,]).circle),
		];

		bufsigs = bufsigs * bufenvs;

		kwargs[\synth][\sig] = kwargs[\synth][\sig] + Mix.ar(bufsigs);

	}, *[ // set default kwargs:
		numChannels:2,
		bufnum:nil, // should be defined when factory created
		rate:1,
	]);


});
)
