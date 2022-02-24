import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { QuarqTestModule } from '../../../test.module';
import { FileComponent } from 'app/entities/file/file.component';
import { FileService } from 'app/entities/file/file.service';
import { File } from 'app/shared/model/file.model';

describe('Component Tests', () => {
  describe('File Management Component', () => {
    let comp: FileComponent;
    let fixture: ComponentFixture<FileComponent>;
    let service: FileService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [QuarqTestModule],
        declarations: [FileComponent],
      })
        .overrideTemplate(FileComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FileComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FileService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new File(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.files && comp.files[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
