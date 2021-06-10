jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISubRule, SubRule } from '../sub-rule.model';
import { SubRuleService } from '../service/sub-rule.service';

import { SubRuleRoutingResolveService } from './sub-rule-routing-resolve.service';

describe('Service Tests', () => {
  describe('SubRule routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: SubRuleRoutingResolveService;
    let service: SubRuleService;
    let resultSubRule: ISubRule | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(SubRuleRoutingResolveService);
      service = TestBed.inject(SubRuleService);
      resultSubRule = undefined;
    });

    describe('resolve', () => {
      it('should return ISubRule returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSubRule = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSubRule).toEqual({ id: 123 });
      });

      it('should return new ISubRule if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSubRule = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultSubRule).toEqual(new SubRule());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSubRule = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSubRule).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
