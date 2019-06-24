(

ClioLibrary.catalog ([\func, \buf], {

	~rateIr = { arg kwargs; kwargs[\rate] = \rate.ir( 1 ) };

	~rateKr = { arg kwargs; kwargs[\rate] = \rate.kr( 1 ) };

	// scales the rate of playback based on ratio of freq to a pre-defined centerFreq
	// HOW COOL!
	~rateScale = { arg kwargs;

		var centerFreq = \centerFreq.ir( kwargs[\centerFreq] ? 440 );
		var rateScale = \rateScale.ir( kwargs[\rateScale] ? 0.5 );

		kwargs[\rate] = (kwargs[\rate] ? 1) * ((kwargs[\freq]/centerFreq)**rateScale);

	};

	~play = { arg kwargs;
		var doneAction = kwargs[\def_play_doneAction] ? 2;

		var numChannels = kwargs[\def_numChannels] ? 2;
		var bufnum = \bufnum.ir( kwargs[\bufnum] );
		var start = \start.ir( kwargs[\start] ? 0);
		var rate = kwargs[\rate] ? 1;

		kwargs[\sig] = kwargs[\sig] + PlayBuf.ar(numChannels,
			bufnum:bufnum,
			rate:BufRateScale.kr(bufnum) * rate,
			startPos:BufSampleRate.ir(bufnum) * start,
			doneAction:doneAction,
		);


	};

	~drone = { arg kwargs;

		var numChannels = kwargs[\def_numChannels] ? 2;
		var bufnum = kwargs[\bufnum];

		var bufsigs, bufenvs;
		var length = BufDur.ir(bufnum);
		var rate = kwargs[\rate] ? 1;

		var envTimes = (length / rate / 4)!3;

		bufsigs = 4.collect{ |i|
			PlayBuf.ar(numChannels,
				bufnum:bufnum,
				rate:BufRateScale.ir(bufnum)*rate,
				startPos:BufSampleRate.ir(bufnum) * length * i/4,
				loop:1);
		};

		bufenvs = [
			EnvGen.kr(Env.new(levels: [0, 0.25, 0.5, 0.25, 0], times: envTimes, curve: [6,-6,6,-6,]).circle),
			EnvGen.kr(Env.new(levels: [0.25, 0.5, 0.25, 0, 0.25], times: envTimes, curve: [-6,6,-6,6]).circle),
			EnvGen.kr(Env.new(levels: [0.5, 0.25, 0, 0.25, 0.5], times: envTimes, curve: [6,-6,6,-6]).circle),
			EnvGen.kr(Env.new(levels: [0.25, 0, 0.25, 0.5, 0.25], times: envTimes, curve: [-6,6,-6, 6,]).circle),
		];

		bufsigs = bufsigs * bufenvs;

		kwargs[\sig] = kwargs[\sig] + Mix.ar(bufsigs);
	}


});
)
