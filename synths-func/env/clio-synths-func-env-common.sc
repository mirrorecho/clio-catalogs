(

ClioLibrary.catalog ([\func, \env, \common], {


	~perc = { arg kwargs;
		var env = EnvGen.kr(Env.perc(
			attackTime: \attackTime.ir( kwargs[\attackTime] ? 0.01 ),
			releaseTime: \releaseTime.ir( kwargs[\releaseTime] ? 2 ),
			level:kwargs[\amp],
			curve: \curve.ir( kwargs[\curve] ? -4 ),
		), doneAction:2);

		kwargs[\sig] = kwargs[\sig] * env;
	};


	~asr = { arg kwargs;
		var env = EnvGen.kr(Env.asr(
			attackTime: \attackTime.ir( kwargs[\attackTime] ? 0.01 ),
			sustainLevel:kwargs[\amp],
			releaseTime: \releaseTime.ir( kwargs[\releaseTime] ? 2 ),
			curve: \curve.ir( kwargs[\curve] ? -4 ),
		), gate:kwargs[\gate], doneAction:2);

		kwargs[\sig] = kwargs[\sig] * env;
	};


	~adsr = { arg kwargs;
		var env = EnvGen.kr(Env.adsr(
			attackTime: \attackTime.ir( kwargs[\attackTime] ? 0.01 ),
			decayTime: \decayTime.ir( kwargs[\decayTime] ? 0.3 ),
			sustainLevel: \sustainLevel.ir( kwargs[\sustainLevel] ? 0.69 ),
			releaseTime: \releaseTime.ir( kwargs[\releaseTime] ? 2 ),
			peakLevel: kwargs[\amp],
			curve: \curve.ir( kwargs[\curve] ? -4 ),
		), gate:kwargs[\gate], doneAction:2);

		kwargs[\sig] = kwargs[\sig] * env;
	};


	~swell = { arg kwargs;
		var env = EnvGen.kr(Env.perc(
			attackTime: \attackTime.ir( kwargs[\attackTime] ? 2 ),
			releaseTime: \releaseTime.ir( kwargs[\releaseTime] ? 0.01 ),
			level:kwargs[\amp],
			curve: \curve.ir( kwargs[\curve] ? 2 ),
		), doneAction:2);

		kwargs[\sig] = kwargs[\sig] * env;
	};

	~silenceFree = { arg kwargs;
		DetectSilence.ar( kwargs[\sig], doneAction: 2 );
	};


});


)
