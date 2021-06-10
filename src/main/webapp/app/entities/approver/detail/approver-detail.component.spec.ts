import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ApproverDetailComponent } from './approver-detail.component';

describe('Component Tests', () => {
  describe('Approver Management Detail Component', () => {
    let comp: ApproverDetailComponent;
    let fixture: ComponentFixture<ApproverDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ApproverDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ approver: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ApproverDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ApproverDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load approver on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.approver).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
