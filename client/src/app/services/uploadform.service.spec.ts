import { TestBed } from '@angular/core/testing';

import { UploadformService } from './uploadform.service';

describe('UploadformService', () => {
  let service: UploadformService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UploadformService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
